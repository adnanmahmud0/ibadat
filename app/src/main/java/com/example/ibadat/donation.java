package com.example.ibadat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class donation extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        EditText editTextTransactionId = findViewById(R.id.editTextTransactionId);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String transactionId = editTextTransactionId.getText().toString();


                if (userName.isEmpty() || phoneNumber.isEmpty() || transactionId.isEmpty()) {
                    Toast.makeText(donation.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {

                    saveDonationToDatabase(userName, phoneNumber, transactionId);
                    Toast.makeText(donation.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDonationToDatabase(String userName, String phoneNumber, String transactionId) {
        mDatabase.child("donations").orderByChild("transactionId").equalTo(transactionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(donation.this, "Transaction ID already exists", Toast.LENGTH_SHORT).show();
                } else {

                    Donation donation = new Donation(userName, phoneNumber, transactionId);
                    mDatabase.child("donations").push().setValue(donation);


                    Toast.makeText(donation.this, "Payment Complete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }


    public class Donation {
        private String userName;
        private String phoneNumber;
        private String transactionId;


        public Donation(String userName, String phoneNumber, String transactionId) {
            this.userName = userName;
            this.phoneNumber = phoneNumber;
            this.transactionId = transactionId;
        }

        public String getUserName() {
            return userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getTransactionId() {
            return transactionId;
        }
    }

    public void searchfunc(View view) {
        Intent myIntent = new Intent(this, search.class);
        startActivity(myIntent);
    }

    public void back(View view) {
        finish();
    }
}
