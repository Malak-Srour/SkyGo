package com.example.skygo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skygo.R;
import com.example.skygo.adapters.FlightAdapter;
import com.example.skygo.models.Flight;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FlightResultsActivity extends AppCompatActivity {

    // Fields...
    private RecyclerView recyclerViewFlights;
    private TextView textViewNoFlights, textViewSearchCriteria;
    private ProgressBar progressBar;
    private Button buttonBack;

    private List<Flight> flightList = new ArrayList<>();
    private FlightAdapter flightAdapter;
    private DatabaseReference databaseReference;

    private String origin, destination, date, flightClass;
    private int adults, children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerViewFlights = findViewById(R.id.recycler_view_flights);
        textViewNoFlights = findViewById(R.id.text_view_no_flights);
        textViewSearchCriteria = findViewById(R.id.text_view_search_criteria);
        progressBar = findViewById(R.id.progress_bar);
        buttonBack = findViewById(R.id.button_back);

        getSearchCriteria();
        displaySearchCriteria();
        setupRecyclerView();

        buttonBack.setOnClickListener(v -> finish());

        loadFlights();
    }

    private void getSearchCriteria() {
        Intent intent = getIntent();
        origin = intent.getStringExtra("origin");
        destination = intent.getStringExtra("destination");
        date = intent.getStringExtra("date");
        flightClass = intent.getStringExtra("flightClass");
        adults = intent.getIntExtra("adults", 1);
        children = intent.getIntExtra("children", 0);
    }

    private void displaySearchCriteria() {
        String criteria = extractCityCode(origin) + " → " + extractCityCode(destination) +
                " | " + date + " | " + flightClass + " | " + adults + " Adult" + (adults > 1 ? "s" : "");
        if (children > 0) {
            criteria += ", " + children + " Child" + (children > 1 ? "ren" : "");
        }
        textViewSearchCriteria.setText(criteria);
    }

    private String extractCityCode(String cityWithCode) {
        int startIndex = cityWithCode.lastIndexOf("(");
        int endIndex = cityWithCode.lastIndexOf(")");
        if (startIndex != -1 && endIndex != -1) {
            return cityWithCode.substring(startIndex + 1, endIndex);
        }
        return cityWithCode;
    }

    private void setupRecyclerView() {
        flightAdapter = new FlightAdapter(flightList, new FlightAdapter.OnFlightClickListener() {
            @Override
            public void onFlightClick(Flight flight) {
                Intent intent = new Intent(FlightResultsActivity.this, SeatSelectionActivity.class);
                intent.putExtra("flightId", flight.getFlightId());
                intent.putExtra("airline", flight.getAirline());
                intent.putExtra("origin", flight.getOrigin());
                intent.putExtra("destination", flight.getDestination());
                intent.putExtra("departureTime", flight.getDepartureTime());
                intent.putExtra("arrivalTime", flight.getArrivalTime());
                intent.putExtra("date", flight.getDate());
                intent.putExtra("price", flight.getPrice());
                intent.putExtra("flightClass", flight.getFlightClass());
                intent.putExtra("adults", adults);
                intent.putExtra("children", children);
                startActivity(intent);
            }

            @Override
            public void onEditClick(Flight flight) { }

            @Override
            public void onDeleteClick(Flight flight) { }
        }, false);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFlights.setAdapter(flightAdapter);
    }

    private void loadFlights() {
        progressBar.setVisibility(View.VISIBLE);
        String originCode = extractCityCode(origin);
        String destinationCode = extractCityCode(destination);

        databaseReference.child("flights")
                .orderByChild("origin")
                .equalTo(originCode)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        flightList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Flight flight = snapshot.getValue(Flight.class);
                            if (flight != null &&
                                    flight.getDestination().equals(destinationCode) &&
                                    flight.getDate().equals(date) &&
                                    flight.getFlightClass().equals(flightClass)) {
                                flightList.add(flight);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        if (flightList.isEmpty()) {
                            textViewNoFlights.setVisibility(View.VISIBLE);
                            recyclerViewFlights.setVisibility(View.GONE);
                        } else {
                            textViewNoFlights.setVisibility(View.GONE);
                            recyclerViewFlights.setVisibility(View.VISIBLE);
                            flightAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        textViewNoFlights.setVisibility(View.VISIBLE);
                        textViewNoFlights.setText("Error loading flights: " + databaseError.getMessage());
                    }
                });
    }
}
