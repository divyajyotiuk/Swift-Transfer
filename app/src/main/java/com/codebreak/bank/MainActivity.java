package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

String uid;
    MaterialCardView sendMoney, addMoney, checkBalance, transactionHistory;
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
        transactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, TransactionHistoryActivity.class);
                startActivity(myIntent);
            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("WUAccount").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balance.setText(dataSnapshot.child("walletBalance").getValue().toString());
                name.setText(dataSnapshot.child("fullName").getValue().toString().split("\\s+")[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SendMoneyActivity.class);
                startActivity(myIntent);
            }
        });
        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }




}
