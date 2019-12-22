package com.codebreak.bank;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CheckBalanceActivity extends AppCompatActivity {

    ProgressBar progressBar;;
    Toolbar toolbar;
    TextView balance;
    TextView currency;
    private FirebaseDatabase secondaryDatabase;
    private DatabaseReference secondaryRef, primaryRef;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);
        progressBar = findViewById(R.id.progress);
        View view = findViewById(android.R.id.content);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.yellow_500));
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        balance = findViewById(R.id.amount);
        currency = findViewById(R.id.currency);
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        secondaryDatabase = FirebaseDatabase.getInstance(app);
        secondaryRef = secondaryDatabase.getReference("accounts");
        primaryRef = FirebaseDatabase.getInstance().getReference().child("WUAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        secondaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.child("phoneNo").getValue().toString().equals(phone))
                    {
                        balance.setText(dataSnapshot1.child("balance").getValue().toString());
                        primaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                currency.setText(dataSnapshot.child("currency").getValue().toString());
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
