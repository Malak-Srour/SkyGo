package com.example.skygo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.skygo.R;
import com.example.skygo.adapters.ReservationAdapter;
import com.example.skygo.models.Reservation;
import com.google.firebase.database.*;

import java.util.*;

public class ManageReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReservations;
    private List<Reservation> reservationList = new ArrayList<>();
    private ReservationAdapter adapter;
    private DatabaseReference ticketsRef;

    Button buttonBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservations);

        buttonBackToDashboard = findViewById(R.id.buttonBackToDashboard);

        buttonBackToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ManageReservationsActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        });
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));

        ticketsRef = FirebaseDatabase.getInstance().getReference("tickets");

        adapter = new ReservationAdapter(reservationList);
        recyclerViewReservations.setAdapter(adapter);


        loadReservations();
    }

    private void loadReservations() {
        ticketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reservationList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Reservation reservation = snap.getValue(Reservation.class);
                    if (reservation != null) {
                        reservationList.add(reservation);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ManageReservationsActivity.this, "Failed to load reservations", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
