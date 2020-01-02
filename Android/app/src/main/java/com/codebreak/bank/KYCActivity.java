package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    AutoCompleteTextView actvBank, actvIdproof, actvCountry;
    ProgressBar progress;
    Button btnKyc;
    String bank, idProofType;
    private ArrayAdapter<String> bankAdapter, idProofAdapter, usaBankAdapter, ukBankAdapter;
    private FirebaseDatabase secondaryDatabase;
    private DatabaseReference secondaryRef;
    private String phoneNo;
    private ArrayAdapter<String>  usaAdapter, ukAdapter;

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
        usaBankAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.usa_bank));
        ukBankAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.uk_bank));

      //  actvBank.setAdapter(bankAdapter);
        phoneNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String countryCode = PhoneUtils.getCountryIsoForCountryCode(PhoneUtils.getCountryCodeForPhoneNumber(phoneNo));

        actvBank.setOnItemClickListener((parent, view, position, id) -> bank = actvBank.getAdapter().getItem(position).toString());
        idProofAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.india_id_proof));
        usaAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.us_id_proof));
        ukAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.uk_id_proof));
        //actvIdproof.setAdapter(idProofAdapter);

        actvIdproof.setOnItemClickListener((parent, view, position, id) -> idProofType = actvIdproof.getAdapter().getItem(position).toString());
        btnRoot = findViewById(R.id.btn_root);
        btnKyc = findViewById(R.id.btn_kyc);
        btnKyc.setOnClickListener(v -> {

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
                        if(dataSnapshot1.child("phoneNo").getValue().toString().equals(phoneNo) && dataSnapshot1.child("bankName").getValue().toString().equals(bank))
                        {

                            progressText.setText("Storing the information");
                            User user = new User();
                            String countryCode1 = PhoneUtils.getCountryIsoForCountryCode(PhoneUtils.getCountryCodeForPhoneNumber(phoneNo));
                            user.setData(phoneNo, countryCode1,idProofType,bank,idProofNo.getText().toString(),getCurrencyCode(countryCode1));
                            user.setFullName(dataSnapshot1.child("name").getValue().toString());
                            user.generateUserID();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                            startActivity(new Intent(KYCActivity.this, SetPinActivity.class));
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
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







        });

        FirebaseApp app = FirebaseApp.getInstance("secondary");
        // Get the database for the other app.
        secondaryDatabase = FirebaseDatabase.getInstance(app);

        secondaryRef = secondaryDatabase.getReference("accounts");
        Toast.makeText(this, countryCode, Toast.LENGTH_SHORT).show();
        switch (countryCode)
        {
            case "US":
                actvIdproof.setAdapter(usaAdapter);
                actvBank.setAdapter(usaBankAdapter);
                break;
            case "GB":
                actvIdproof.setAdapter(ukAdapter);
                actvBank.setAdapter(ukBankAdapter);
                break;
            case "IN":
                actvIdproof.setAdapter(idProofAdapter);
                actvBank.setAdapter(bankAdapter);
                break;
        }

    }

    public static String getCurrencyCode(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }

}
