package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codebreak.bank.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Currency;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class KYCActivity extends AppCompatActivity {

    ViewGroup btnRoot, progressParent, infoParent, root;
    TextView progressText;
    TextInputEditText idProofNo;
    AutoCompleteTextView actvBank, actvIdproof;
    ProgressBar progress;
    Button btnKyc;
    String bank, idProofType;
    private ArrayAdapter<String> bankAdapter, idProofAdapter;
    private FirebaseDatabase secondaryDatabase;
    private DatabaseReference secondaryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        actvBank = findViewById(R.id.actv_bank);
        root = findViewById(android.R.id.content);
        infoParent = findViewById(R.id.info_parent);
        idProofNo = findViewById(R.id.id_proof_no);
        progressParent = findViewById(R.id.progress_parent);
        progressText = findViewById(R.id.tv_progress);
        progress = findViewById(R.id.progress);
        actvIdproof = findViewById(R.id.actv_id_proof);
        bankAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.bank));
        actvBank.setAdapter(bankAdapter);
        actvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bank = bankAdapter.getItem(position);
            }
        });
        idProofAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.id_proof));
        actvIdproof.setAdapter(idProofAdapter);
        actvIdproof.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idProofType = idProofAdapter.getItem(position);
            }
        });
        btnRoot = findViewById(R.id.btn_root);
        btnKyc = findViewById(R.id.btn_kyc);
        btnKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Transition transition = new AutoTransition();
//                transition.setDuration(200L);
//                transition.setInterpolator(new FastOutLinearInInterpolator());
                TransitionManager.beginDelayedTransition(root);
                infoParent.setVisibility(View.GONE);
                progressParent.setVisibility(View.VISIBLE);
                progressText.setText("Checking with bank");

                secondaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            if(dataSnapshot1.child("phoneNo").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                            {
                                progressText.setText("Storing the information");
                                User user = new User();
                                String phoneNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                String countryCode = PhoneUtils.getCountryIsoForCountryCode(PhoneUtils.getCountryCodeForPhoneNumber(phoneNo));
                                user.setData(phoneNo,countryCode,idProofType,bank,idProofNo.getText().toString(),getCurrencyCode(countryCode));
                                user.setFullName(dataSnapshot1.child("name").getValue().toString());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                startActivity(new Intent(KYCActivity.this, MainActivity.class));
                                finish();
                                flag=1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            Snackbar.make(root,"Sorry, we couldn't verify your bank account.", Snackbar.LENGTH_SHORT).show();
                            TransitionManager.beginDelayedTransition(root);
                            infoParent.setVisibility(View.VISIBLE);
                            progressParent.setVisibility(View.GONE);
                            FirebaseAuth.getInstance().signOut();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







            }
        });

        FirebaseApp app = FirebaseApp.getInstance("secondary");
        // Get the database for the other app.
        secondaryDatabase = FirebaseDatabase.getInstance(app);

        secondaryRef = secondaryDatabase.getReference("accounts");



    }

    public static String getCurrencyCode(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }

}
