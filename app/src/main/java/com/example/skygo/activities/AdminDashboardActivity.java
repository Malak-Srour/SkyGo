package com.example.skygo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.skygo.R;
import com.example.skygo.adapters.FlightAdapter;
import com.example.skygo.models.Flight;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import java.util.*;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView textViewGreeting;
    private RecyclerView recyclerViewFlights;
    private FloatingActionButton buttonAddFlight;

    private FlightAdapter adapter;
    private List<Flight> flightList = new ArrayList<>();
    private DatabaseReference flightsRef;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        textViewGreeting = findViewById(R.id.textViewAdminGreeting);
        recyclerViewFlights = findViewById(R.id.recyclerViewFlights);
        buttonAddFlight = findViewById(R.id.buttonAddFlight); // ✅ Initialize it here

        flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        setupRecyclerView();
        loadFlights();

        Button buttonManageUsers = findViewById(R.id.buttonManageUsers);
        buttonManageUsers.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, ManageUsersActivity.class));
        });

        Button buttonViewReservations = findViewById(R.id.buttonViewReservations);
        buttonViewReservations.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, ManageReservationsActivity.class));
        });


        // ✅ Now this is inside onCreate
        buttonAddFlight.setOnClickListener(v -> {
            startActivity(new Intent(AdminDashboardActivity.this, AddFlightActivity.class));


        });

        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(v -> {
            // Logout logic (if you’re using Firebase Auth, you can also sign out)
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // رجوع على شاشة اختيار الدور أو تسجيل الدخول
            Intent intent = new Intent(AdminDashboardActivity.this, RoleSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // يمسح back stack
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FlightAdapter(flightList, new FlightAdapter.OnFlightClickListener() {
            @Override
            public void onFlightClick(Flight flight) {
                // Optional: show details or do nothing
            }

            @Override
            public void onEditClick(Flight flight) {
                // Launch EditFlightActivity and pass flight ID
                Intent intent = new Intent(AdminDashboardActivity.this, EditFlightActivity.class);
                intent.putExtra("flightId", flight.getFlightId()); // Assuming getFlightId() exists
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Flight flight) {
                // Delete the flight from Firebase
                flightsRef.child(flight.getFlightId()).removeValue()
                        .addOnSuccessListener(unused -> Toast.makeText(AdminDashboardActivity.this, "Flight deleted", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(AdminDashboardActivity.this, "Failed to delete flight", Toast.LENGTH_SHORT).show());
            }
        }, true); // ✅ Set isAdmin = true

        recyclerViewFlights.setAdapter(adapter);
    }


    private void loadFlights() {
        flightsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                flightList.clear();
                for (DataSnapshot flightSnap : snapshot.getChildren()) {
                    Flight flight = flightSnap.getValue(Flight.class);
                    if (flight != null) {
                        flightList.add(flight);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load flights", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
