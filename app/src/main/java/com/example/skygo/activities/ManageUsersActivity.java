package com.example.skygo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.skygo.R;
import com.example.skygo.adapters.UserAdapter;
import com.example.skygo.models.User;
import com.google.firebase.database.*;

import java.util.*;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private List<User> userList = new ArrayList<>();
    private UserAdapter adapter;
    private DatabaseReference usersRef;
    private Button buttonBackToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        buttonBackToDashboard = findViewById(R.id.buttonBackToDashboard);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        setupRecyclerView();
        loadUsers();

        buttonBackToDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ManageUsersActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupRecyclerView() {
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList, user -> showDeleteConfirmation(user));
        recyclerViewUsers.setAdapter(adapter);
    }

    private void loadUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null && !user.getEmail().equals("admin@skygo.com")) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getFullName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    usersRef.child(user.getUserId()).removeValue()
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
