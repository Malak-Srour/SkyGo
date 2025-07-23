package com.example.skygo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skygo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerOrigin, spinnerDestination, spinnerClass;
    private EditText editTextDate;
    private TextView textViewAdults, textViewChildren, textViewWelcome;
    private Button buttonSearchFlights, buttonAdultsPlus, buttonAdultsMinus,
            buttonChildrenPlus, buttonChildrenMinus;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Calendar selectedDate;
    private int adultsCount = 1;
    private int childrenCount = 0;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        initializeViews();

        // Set up spinners
        setupSpinners();

        // Set up date picker
        setupDatePicker();

        // Set up passenger counters
        setupPassengerCounters();

        // Set up search button
        setupSearchButton();

        // Display welcome message
        displayWelcomeMessage();

        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(v -> {
            // إذا عم تستخدم Firebase Authentication:
            // FirebaseAuth.getInstance().signOut();

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, RoleSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // مسح back stack
            startActivity(intent);
            finish();
        });

    }

    private void initializeViews() {
        spinnerOrigin = findViewById(R.id.spinner_origin);
        spinnerDestination = findViewById(R.id.spinner_destination);
        spinnerClass = findViewById(R.id.spinner_class);
        editTextDate = findViewById(R.id.edit_text_date);
        textViewAdults = findViewById(R.id.text_view_adults);
        textViewChildren = findViewById(R.id.text_view_children);
        textViewWelcome = findViewById(R.id.text_view_welcome);
        buttonSearchFlights = findViewById(R.id.button_search_flights);
        buttonAdultsPlus = findViewById(R.id.button_adults_plus);
        buttonAdultsMinus = findViewById(R.id.button_adults_minus);
        buttonChildrenPlus = findViewById(R.id.button_children_plus);
        buttonChildrenMinus = findViewById(R.id.button_children_minus);

        // Initialize calendar
        selectedDate = Calendar.getInstance();
    }

    private void setupSpinners() {
        spinnerOrigin = findViewById(R.id.spinner_origin);
        spinnerDestination = findViewById(R.id.spinner_destination);
        spinnerClass = findViewById(R.id.spinner_class);

        ArrayList<String> originList = new ArrayList<>();
        ArrayList<String> destinationList = new ArrayList<>();

        ArrayAdapter<String> originAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, originList);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinationList);
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrigin.setAdapter(originAdapter);
        spinnerDestination.setAdapter(destinationAdapter);

        // Load from Firebase
        databaseReference.child("flights").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashSet<String> originSet = new HashSet<>();
                HashSet<String> destinationSet = new HashSet<>();

                for (DataSnapshot flightSnap : snapshot.getChildren()) {
                    String origin = flightSnap.child("origin").getValue(String.class);
                    String destination = flightSnap.child("destination").getValue(String.class);

                    if (origin != null) originSet.add(origin);
                    if (destination != null) destinationSet.add(destination);
                }

                originList.clear();
                originList.addAll(originSet);

                destinationList.clear();
                destinationList.addAll(destinationSet);

                originAdapter.notifyDataSetChanged();
                destinationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
            }
        });

        // Flight classes remain static
        String[] classes = {"Economy", "Premium Economy", "Business", "First Class"};
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
    }


    private void setupDatePicker() {
        // Set initial date to today
        updateDateDisplay();

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Make it non-editable by keyboard
        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateDisplay();
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // Set maximum date to 1 year from now
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, 1);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editTextDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void setupPassengerCounters() {
        updatePassengerDisplay();

        buttonAdultsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adultsCount < 9) {
                    adultsCount++;
                    updatePassengerDisplay();
                }
            }
        });

        buttonAdultsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adultsCount > 1) {
                    adultsCount--;
                    updatePassengerDisplay();
                }
            }
        });

        buttonChildrenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childrenCount < 8) {
                    childrenCount++;
                    updatePassengerDisplay();
                }
            }
        });

        buttonChildrenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childrenCount > 0) {
                    childrenCount--;
                    updatePassengerDisplay();
                }
            }
        });
    }

    private void updatePassengerDisplay() {
        textViewAdults.setText(String.valueOf(adultsCount));
        textViewChildren.setText(String.valueOf(childrenCount));
    }

    private void setupSearchButton() {
        buttonSearchFlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFlights();
            }
        });
    }

    private void searchFlights() {
        String origin = spinnerOrigin.getSelectedItem().toString();
        String destination = spinnerDestination.getSelectedItem().toString();
        String flightClass = spinnerClass.getSelectedItem().toString();
        String date = editTextDate.getText().toString();

        // Validate selections
        if (origin.equals(destination)) {
            Toast.makeText(this, "Origin and destination cannot be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create intent for flight results
        Intent intent = new Intent(MainActivity.this, FlightResultsActivity.class);
        intent.putExtra("origin", origin);
        intent.putExtra("destination", destination);
        intent.putExtra("date", date);
        intent.putExtra("flightClass", flightClass);
        intent.putExtra("adults", adultsCount);
        intent.putExtra("children", childrenCount);

        startActivity(intent);
    }

    private void displayWelcomeMessage() {
        if (firebaseAuth.getCurrentUser() != null) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            textViewWelcome.setText("Welcome, " + email + "!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}