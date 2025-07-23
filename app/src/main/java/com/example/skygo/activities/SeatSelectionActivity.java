package com.example.skygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import com.example.skygo.R;
import com.example.skygo.adapters.SeatAdapter;
import com.example.skygo.models.Flight;
import com.example.skygo.models.Seat;
import com.example.skygo.models.Ticket;
import com.google.firebase.database.*;

import java.text.NumberFormat;
import java.util.*;

public class SeatSelectionActivity extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {

    private Button buttonBack, buttonConfirmBooking;
    private TextView textViewFlightInfo, textViewSelectedSeat, textViewTotalPrice;
    private EditText editTextPassengerName, editTextPassengerEmail;
    private RecyclerView recyclerViewSeats;

    private Flight selectedFlight;
    private int adults, children;
    private List<Seat> seatList = new ArrayList<>();
    private List<String> selectedSeats = new ArrayList<>();
    private SeatAdapter seatAdapter;

    private DatabaseReference flightRef, ticketsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Firebase
        flightRef = FirebaseDatabase.getInstance().getReference("flights");
        ticketsRef = FirebaseDatabase.getInstance().getReference("tickets");

        // UI Components
        buttonBack = findViewById(R.id.button_back);
        buttonConfirmBooking = findViewById(R.id.button_confirm_booking);
        textViewFlightInfo = findViewById(R.id.text_view_flight_info);
        textViewSelectedSeat = findViewById(R.id.text_view_selected_seat);
        textViewTotalPrice = findViewById(R.id.text_view_total_price);
        editTextPassengerName = findViewById(R.id.edit_text_passenger_name);
        editTextPassengerEmail = findViewById(R.id.edit_text_passenger_email);
        recyclerViewSeats = findViewById(R.id.recycler_view_seats);

        // Get data from Intent
        getFlightDataFromIntent();

        // Flight display
        displayFlightInfo();

        // RecyclerView config
        recyclerViewSeats.setLayoutManager(new GridLayoutManager(this, 6));
        seatAdapter = new SeatAdapter(seatList, this);
        recyclerViewSeats.setAdapter(seatAdapter);

        loadSeatsFromFirebase();

        // Button listeners
        buttonBack.setOnClickListener(v -> finish());
        buttonConfirmBooking.setOnClickListener(v -> {
            if (validateBookingDetails()) {
                bookSeats();
            }
        });
    }

    private void getFlightDataFromIntent() {
        Intent intent = getIntent();
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

        adults = intent.getIntExtra("adults", 1);
        children = intent.getIntExtra("children", 0);
    }

    private void displayFlightInfo() {
        String flightInfo = selectedFlight.getAirline() + " " + selectedFlight.getFlightId() + "\n" +
                selectedFlight.getOrigin() + " → " + selectedFlight.getDestination() + "\n" +
                "Date: " + selectedFlight.getDate() + " | Class: " + selectedFlight.getFlightClass() + "\n" +
                "Departure: " + selectedFlight.getDepartureTime() + " - Arrival: " + selectedFlight.getArrivalTime();

        textViewFlightInfo.setText(flightInfo);
        updateTotalPrice();
    }

    private void loadSeatsFromFirebase() {
        flightRef.child(selectedFlight.getFlightId()).child("seats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                generateSeats(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeatSelectionActivity.this, "Error loading seats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateSeats(DataSnapshot snapshot) {
        seatList.clear();
        for (int row = 1; row <= 10; row++) {
            for (char col = 'A'; col <= 'F'; col++) {
                String seatId = row + String.valueOf(col);
                boolean isBooked = snapshot.child(seatId).getValue(Boolean.class) != null &&
                        snapshot.child(seatId).getValue(Boolean.class);
                seatList.add(new Seat(seatId, isBooked, false));
            }
        }
        seatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSeatClick(Seat seat) {
        if (seat.isOccupied()) {
            Toast.makeText(this, "Seat already booked", Toast.LENGTH_SHORT).show();
            return;
        }

        String seatId = seat.getSeatNumber();
        if (seat.isSelected()) {
            seat.setSelected(false);
            selectedSeats.remove(seatId);
        } else {
            if (selectedSeats.size() >= adults + children) {
                Toast.makeText(this, "You can only select " + (adults + children) + " seats", Toast.LENGTH_SHORT).show();
                return;
            }
            seat.setSelected(true);
            selectedSeats.add(seatId);
        }

        seatAdapter.notifyDataSetChanged();
        updateSelectedSeatsInfo();
    }

    private void updateSelectedSeatsInfo() {
        textViewSelectedSeat.setText("Selected: " + (selectedSeats.isEmpty() ? "None" : String.join(", ", selectedSeats)));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = selectedFlight.getPrice() * (adults + children);
        textViewTotalPrice.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(total));
    }

    private boolean validateBookingDetails() {
        if (TextUtils.isEmpty(editTextPassengerName.getText().toString().trim())) {
            editTextPassengerName.setError("Enter passenger name");
            return false;
        }
        String email = editTextPassengerEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextPassengerEmail.setError("Valid email required");
            return false;
        }
        if (selectedSeats.size() != (adults + children)) {
            Toast.makeText(this, "Select exactly " + (adults + children) + " seats", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void bookSeats() {
        Map<String, Object> seatUpdates = new HashMap<>();
        for (String seatId : selectedSeats) {
            seatUpdates.put("seats/" + seatId, true);
        }

        flightRef.child(selectedFlight.getFlightId()).updateChildren(seatUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                createTicket();
            } else {
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTicket() {
        String ticketId = ticketsRef.push().getKey();
        if (ticketId == null) return;

        Ticket ticket = new Ticket(
                ticketId,
                selectedFlight.getFlightId(),
                editTextPassengerName.getText().toString().trim(),
                editTextPassengerEmail.getText().toString().trim(),
                String.join(", ", selectedSeats),
                selectedFlight.getPrice() * (adults + children)
        );

        ticketsRef.child(ticketId).setValue(ticket).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(this, ConfirmationActivity.class);

            // Flight info
                intent.putExtra("flightId", selectedFlight.getFlightId());
                intent.putExtra("airline", selectedFlight.getAirline());
                intent.putExtra("origin", selectedFlight.getOrigin());
                intent.putExtra("destination", selectedFlight.getDestination());
                intent.putExtra("departureTime", selectedFlight.getDepartureTime());
                intent.putExtra("arrivalTime", selectedFlight.getArrivalTime());
                intent.putExtra("date", selectedFlight.getDate());
                intent.putExtra("price", selectedFlight.getPrice());
                intent.putExtra("flightClass", selectedFlight.getFlightClass());

            // Passenger info
                intent.putExtra("passengerName", editTextPassengerName.getText().toString().trim());
                intent.putExtra("passengerEmail", editTextPassengerEmail.getText().toString().trim());
                intent.putExtra("selectedSeat", String.join(", ", selectedSeats));
                intent.putExtra("totalPrice", selectedFlight.getPrice() * (adults + children));

            // Ticket info
                intent.putExtra("ticketId", ticketId);

                startActivity(intent);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Ticket save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
