package com.example.skygo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skygo.models.Flight;
import com.example.skygo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ConfirmationActivity extends AppCompatActivity {


    private TextView textViewConfirmationTitle, textViewTicketDetails, textViewFlightDetails,
            textViewPassengerDetails, textViewBookingReference;
    private Button buttonBackToHome, buttonViewTicket;

    private Flight selectedFlight;
    private String ticketId;
    private String passengerName;
    private String passengerEmail;
    private String selectedSeat;
    private double totalPrice;
    private String bookingReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Initialize UI components
        textViewConfirmationTitle = findViewById(R.id.text_view_confirmation_title);
        textViewTicketDetails = findViewById(R.id.text_view_ticket_details);
        textViewFlightDetails = findViewById(R.id.text_view_flight_details);
        textViewPassengerDetails = findViewById(R.id.text_view_passenger_details);
        textViewBookingReference = findViewById(R.id.text_view_booking_reference);
        buttonBackToHome = findViewById(R.id.button_back_to_home);
        buttonViewTicket = findViewById(R.id.button_view_ticket);

        // Get booking data from intent
        getBookingDataFromIntent();

        // Display the confirmation details
        displayConfirmationDetails();

        // Set up button listeners
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(ConfirmationActivity.this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });

        buttonViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTicketEmail();
            }
        });



        Button saveTicketButton = findViewById(R.id.button_save_ticket);
        LinearLayout ticketLayout = findViewById(R.id.ticket_layout); // هذا هو الـ Layout يلي فيه معلومات التذكرة

        saveTicketButton.setOnClickListener(v -> {
            ticketLayout.setDrawingCacheEnabled(true);
            ticketLayout.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(ticketLayout.getDrawingCache());
            ticketLayout.setDrawingCacheEnabled(false);

            String filename = "Ticket_" + System.currentTimeMillis() + ".png";
            OutputStream fos;

            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SkyGo");
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    fos = resolver.openOutputStream(imageUri);
                } else {
                    File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/SkyGo");
                    if (!imagesDir.exists()) imagesDir.mkdirs();
                    File image = new File(imagesDir, filename);
                    fos = new FileOutputStream(image);
                }

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Toast.makeText(this, "Ticket saved to Gallery 🎉", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTicketEmail() {
        if (passengerEmail == null || passengerEmail.isEmpty()) {
            Toast.makeText(this, "No email address provided", Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = "🎫 SkyGo Ticket Confirmation - " + bookingReference;

        String message = "Dear " + passengerName + ",\n\n" +
                "Thank you for booking with SkyGo! 🎉\n\n" +
                "Here are your ticket details:\n\n" +

                "🧾 BOOKING REFERENCE:\n" +
                bookingReference + "\n\n" +

                "✈️ FLIGHT DETAILS:\n" +
                "Flight: " + selectedFlight.getAirline() + " " + selectedFlight.getFlightId() + "\n" +
                "Route: " + selectedFlight.getOrigin() + " → " + selectedFlight.getDestination() + "\n" +
                "Date: " + selectedFlight.getDate() + "\n" +
                "Departure: " + selectedFlight.getDepartureTime() + "\n" +
                "Arrival: " + selectedFlight.getArrivalTime() + "\n" +
                "Class: " + selectedFlight.getFlightClass() + "\n\n" +

                "👤 PASSENGER DETAILS:\n" +
                "Name: " + passengerName + "\n" +
                "Email: " + passengerEmail + "\n" +
                "Seat(s): " + selectedSeat + "\n\n" +

                "🎫 TICKET DETAILS:\n" +
                "Ticket ID: " + ticketId + "\n" +
                "Total Price: $" + String.format("%.2f", totalPrice) + "\n" +
                "Payment: Paid\n" +
                "Booking Date: " + getCurrentDate() + "\n\n" +

                "If you have any questions, feel free to reach out to our support team.\n\n" +
                "Safe travels! ✈️\n" +
                "SkyGo Team";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822"); // Only email clients will respond
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{passengerEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }



    private void getBookingDataFromIntent() {
        Intent intent = getIntent();

        // Get flight data
        selectedFlight = new Flight();
        selectedFlight.setFlightId(intent.getStringExtra("flightId"));
        selectedFlight.setAirline(intent.getStringExtra("airline"));
        selectedFlight.setOrigin(intent.getStringExtra("origin"));
        selectedFlight.setDestination(intent.getStringExtra("destination"));
        selectedFlight.setDepartureTime(intent.getStringExtra("departureTime"));
        selectedFlight.setArrivalTime(intent.getStringExtra("arrivalTime"));
        selectedFlight.setDate(intent.getStringExtra("date"));
        selectedFlight.setPrice(intent.getDoubleExtra("price", 0.0));
        selectedFlight.setFlightClass(intent.getStringExtra("flightClass"));

        // Get booking details
        ticketId = intent.getStringExtra("ticketId");
        passengerName = intent.getStringExtra("passengerName");
        passengerEmail = intent.getStringExtra("passengerEmail");
        selectedSeat = intent.getStringExtra("selectedSeat");
        totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        // Generate booking reference
        bookingReference = "SKY" + System.currentTimeMillis() % 100000 + selectedFlight.getFlightId();
    }

    private void displayConfirmationDetails() {
        // Display confirmation title
        textViewConfirmationTitle.setText("🎉 Booking Confirmed!");

        // Display booking reference
        textViewBookingReference.setText("Booking Reference: " + bookingReference);

        // Display flight details
        String flightDetails = "Flight: " + selectedFlight.getAirline() + " " + selectedFlight.getFlightId() + "\n" +
                "Route: " + selectedFlight.getOrigin() + " → " + selectedFlight.getDestination() + "\n" +
                "Date: " + selectedFlight.getDate() + "\n" +
                "Departure: " + selectedFlight.getDepartureTime() + "\n" +
                "Arrival: " + selectedFlight.getArrivalTime() + "\n" +
                "Class: " + selectedFlight.getFlightClass();
        textViewFlightDetails.setText(flightDetails);

        // Display passenger details
        String passengerDetails = "Passenger: " + passengerName + "\n" +
                "Email: " + passengerEmail + "\n" +
                "Seat: " + selectedSeat;
        textViewPassengerDetails.setText(passengerDetails);

        // Display ticket details
        String ticketDetails = "Ticket ID: " + ticketId + "\n" +
                "Status: Confirmed\n" +
                "Total Price: $" + String.format("%.2f", totalPrice) + "\n" +
                "Payment: Paid\n" +
                "Booking Date: " + getCurrentDate();
        textViewTicketDetails.setText(ticketDetails);
    }

    private void showTicketSummary() {
        String summary = "🎫 TICKET SUMMARY\n\n" +
                "Reference: " + bookingReference + "\n" +
                "Flight: " + selectedFlight.getFlightId() + "\n" +
                "Passenger: " + passengerName + "\n" +
                "Seat: " + selectedSeat + "\n" +
                "Total: $" + String.format("%.2f", totalPrice);

        Toast.makeText(this, summary, Toast.LENGTH_LONG).show();
    }

    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}