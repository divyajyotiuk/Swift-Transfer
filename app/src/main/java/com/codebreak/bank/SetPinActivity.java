package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SetPinActivity extends AppCompatActivity {

    TextInputEditText etPin, etConfirmPin;
    Button done;
    View root;
    ViewGroup progressRoot;
    ProgressBar progressBar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_pin);
        root = findViewById(android.R.id.content);
        etPin = findViewById(R.id.et_enter_pin);
        etConfirmPin = findViewById(R.id.et_confirm_pin);
        done = findViewById(R.id.btn_create_pin);
        progressBar = findViewById(R.id.progress);
        progressRoot = findViewById(R.id.progress_root);
        mDatabase = FirebaseDatabase.getInstance().getReference("WUAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etPin.getText().toString().equals(etConfirmPin.getText().toString()))
                {
                    Snackbar.make(root, "Please enter correct PIN.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    TransitionManager.beginDelayedTransition(progressRoot);
                    progressBar.setVisibility(View.VISIBLE);
                    done.setVisibility(View.INVISIBLE);
                    mDatabase.child("loginPass").setValue(etPin.getText().toString());
                    Intent intent=new Intent(SetPinActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
