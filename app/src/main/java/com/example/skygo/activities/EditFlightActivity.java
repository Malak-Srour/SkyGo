package com.example.skygo.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skygo.R;
import com.example.skygo.models.Flight;
import com.google.firebase.database.*;

public class EditFlightActivity extends AppCompatActivity {

    private EditText airlineInput, originInput, destinationInput, departureTimeInput,
            arrivalTimeInput, dateInput, priceInput, seatsInput, classInput;
    private Button saveButton;

    private DatabaseReference flightRef;
    private String flightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);

        flightId = getIntent().getStringExtra("flightId");
        if (flightId == null) {
            Toast.makeText(this, "Flight ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Views
        airlineInput = findViewById(R.id.input_airline);
        originInput = findViewById(R.id.input_origin);
        destinationInput = findViewById(R.id.input_destination);
        departureTimeInput = findViewById(R.id.input_departure_time);
        arrivalTimeInput = findViewById(R.id.input_arrival_time);
        dateInput = findViewById(R.id.input_date);
        priceInput = findViewById(R.id.input_price);
        seatsInput = findViewById(R.id.input_seats);
        classInput = findViewById(R.id.input_class);
        saveButton = findViewById(R.id.button_save);

        flightRef = FirebaseDatabase.getInstance().getReference("flights").child(flightId);

        loadFlightData();

        saveButton.setOnClickListener(v -> saveFlightChanges());
    }

    private void loadFlightData() {
        flightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Flight flight = snapshot.getValue(Flight.class);
                if (flight != null) {
                    airlineInput.setText(flight.getAirline());
                    originInput.setText(flight.getOrigin());
                    destinationInput.setText(flight.getDestination());
                    departureTimeInput.setText(flight.getDepartureTime());
                    arrivalTimeInput.setText(flight.getArrivalTime());
                    dateInput.setText(flight.getDate());
                    priceInput.setText(String.valueOf(flight.getPrice()));
                    seatsInput.setText(String.valueOf(flight.getAvailableSeats()));
                    classInput.setText(flight.getFlightClass());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditFlightActivity.this, "Error loading flight", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFlightChanges() {
        String airline = airlineInput.getText().toString();
        String origin = originInput.getText().toString();
        String destination = destinationInput.getText().toString();
        String departureTime = departureTimeInput.getText().toString();
        String arrivalTime = arrivalTimeInput.getText().toString();
        String date = dateInput.getText().toString();
        double price = Double.parseDouble(priceInput.getText().toString());
        int seats = Integer.parseInt(seatsInput.getText().toString());
        String flightClass = classInput.getText().toString();

        Flight updatedFlight = new Flight(flightId, airline, origin, destination, departureTime, arrivalTime, date, price, flightClass, seats, seats);

        flightRef.setValue(updatedFlight).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Flight updated", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show());
    }
}
