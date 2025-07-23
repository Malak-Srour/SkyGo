package com.example.skygo.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skygo.R;
import com.example.skygo.models.Flight;
import com.google.firebase.database.*;

public class AddFlightActivity extends AppCompatActivity {

    private EditText flightId, airline, origin, destination, departure, arrival, date, flightClass, totalSeats, price;
    private Button buttonSubmit;

    private DatabaseReference flightsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);

        flightId = findViewById(R.id.editTextFlightId);
        airline = findViewById(R.id.editTextAirline);
        origin = findViewById(R.id.editTextOrigin);
        destination = findViewById(R.id.editTextDestination);
        departure = findViewById(R.id.editTextDeparture);
        arrival = findViewById(R.id.editTextArrival);
        date = findViewById(R.id.editTextDate);
        flightClass = findViewById(R.id.editTextClass);
        totalSeats = findViewById(R.id.editTextSeats);
        price = findViewById(R.id.editTextPrice);
        buttonSubmit = findViewById(R.id.buttonSubmitFlight);

        flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        buttonSubmit.setOnClickListener(v -> submitFlight());
    }

    private void submitFlight() {
        String id = flightId.getText().toString().trim();
        String airlineName = airline.getText().toString().trim();
        String from = origin.getText().toString().trim();
        String to = destination.getText().toString().trim();
        String dep = departure.getText().toString().trim();
        String arr = arrival.getText().toString().trim();
        String fDate = date.getText().toString().trim();
        String fClass = flightClass.getText().toString().trim();
        String seatsStr = totalSeats.getText().toString().trim();
        String priceStr = price.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(airlineName) || TextUtils.isEmpty(from) ||
                TextUtils.isEmpty(to) || TextUtils.isEmpty(dep) || TextUtils.isEmpty(arr) ||
                TextUtils.isEmpty(fDate) || TextUtils.isEmpty(fClass) ||
                TextUtils.isEmpty(seatsStr) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalSeatsInt = Integer.parseInt(seatsStr);
        double priceDouble = Double.parseDouble(priceStr);

        Flight flight = new Flight(id, airlineName, from, to, dep, arr, fDate, priceDouble, fClass, totalSeatsInt);

        flightsRef.child(id).setValue(flight)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Flight added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // go back to admin dashboard
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add flight", Toast.LENGTH_SHORT).show();
                });
    }
}
