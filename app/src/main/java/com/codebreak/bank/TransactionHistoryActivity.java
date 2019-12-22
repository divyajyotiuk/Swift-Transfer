package com.codebreak.bank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.codebreak.bank.model.TxnList;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionHistoryActivity extends AppCompatActivity {


    RecyclerView transactionList;
    private DatabaseReference mFirebaseDatabaseReference;
    private TransactionListAdapter adapter;
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        View view = findViewById(android.R.id.content);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.yellow_500));
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);

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
        adapter = new TransactionListAdapter(options, Math.round(getResources().getDisplayMetrics().scaledDensity * 16), colors, new TransactionListAdapter.onClickListener() {
            @Override
            public void onClick(String txnId) {
                String url = "https://rinkeby.etherscan.io/tx/"+txnId;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
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
