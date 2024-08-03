package com.example.ibadat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class search extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Button searchButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("donations");

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    searchUser(phoneNumber);
                } else {
                    Toast.makeText(search.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchUser(String phoneNumber) {
        databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder userData = new StringBuilder();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String userName = dataSnapshot.child("userName").getValue(String.class);
                        String transactionId = dataSnapshot.child("transactionId").getValue(String.class);
                        userData.append("User Name: ").append(userName).append("\n");
                        userData.append("Transaction ID: ").append(transactionId).append("\n\n");
                    }
                    displayUserData(userData.toString());
                } else {
                    displayUserData("No user found with this phone number");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData(String userData) {
        UserDataFragment userDataFragment = UserDataFragment.newInstance(userData);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, userDataFragment);
        fragmentTransaction.commit();
    }

    public void back(View view) {
        finish();
    }
}
