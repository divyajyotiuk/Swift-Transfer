package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    View root;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = findViewById(android.R.id.content);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(this, PinActivity.class));
            finish();
        }
        else {
            IdpConfig phoneConfigWithDefaultNumber = new IdpConfig.PhoneBuilder()
                    .setDefaultCountryIso("in")
                    .build();
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(phoneConfigWithDefaultNumber))
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).setTheme(R.style.AppTheme).build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                mAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int flag = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            if(dataSnapshot1.child("phoneNo").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                            {
                                flag=1;
                                break;
                            }
                        }
                        if(flag ==0)
                        {
                            startActivity(new Intent(SplashActivity.this, KYCActivity.class));
                            Toast.makeText(SplashActivity.this, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, PinActivity.class));
                            finish();
                        }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    finish();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                showSnackbar("Unknown Error.");
            }
        }
    }

    void showSnackbar(String message)
    {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }
}
