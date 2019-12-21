package com.codebreak.bank;

import android.os.Bundle;

import com.codebreak.bank.model.TxnList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionHistoryActivity extends AppCompatActivity {


    RecyclerView transactionList;
    private DatabaseReference mFirebaseDatabaseReference;
    private TransactionListAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        transactionList = findViewById(R.id.transaction_list);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child("TxnList");
        int[] colors = getResources().getIntArray(R.array.colors);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Query messagesRef = mFirebaseDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        Query messagesRef = mFirebaseDatabaseReference.child("fok349Ki0QO4AFOgeUtBGAg9c0o1");
        FirebaseRecyclerOptions<TxnList> options =
                new FirebaseRecyclerOptions.Builder<TxnList>()
                        .setQuery(messagesRef, TxnList.class)
                        .build();
        adapter = new TransactionListAdapter(options, Math.round(getResources().getDisplayMetrics().scaledDensity*16),colors);
        transactionList.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        adapter.startListening();
    }
}
