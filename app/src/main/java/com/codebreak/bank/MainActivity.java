package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PIN = 250;

    String uid;
    MaterialCardView sendMoney, addMoney, checkBalance, transactionHistory, logOut;
    Button done;
    TextView balance, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMoney = findViewById(R.id.card_send_money);
        addMoney = findViewById(R.id.card_add_money);
        balance = findViewById(R.id.balance);
        name = findViewById(R.id.name);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        checkBalance = findViewById(R.id.card_wallet_balance);
        transactionHistory = findViewById(R.id.card_transaction_history);
        logOut = findViewById(R.id.card_log_out);
        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        });
        transactionHistory.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, TransactionHistoryActivity.class);
            startActivity(myIntent);
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("WUAccount").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                double amount = Math.round((Double.valueOf(dataSnapshot.child("walletBalance").getValue().toString())) * 100.0) / 100.0;

                balance.setText(amount + " "+dataSnapshot.child("currency").getValue().toString());
                name.setText(dataSnapshot.child("fullName").getValue().toString().split("\\s+")[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendMoney.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, SendMoneyActivity.class);
            startActivity(myIntent);
        });
        addMoney.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, AddMoneyActivity.class);
            startActivity(myIntent);
        });
        name.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ProfileActivity .class);
            startActivity(myIntent);
        });

        checkBalance.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, PaymentPinActivity.class), REQUEST_PIN));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIN && resultCode == RESULT_OK) {
            Intent myIntent = new Intent(MainActivity.this, CheckBalanceActivity.class);
            startActivity(myIntent);        }
    }
}
