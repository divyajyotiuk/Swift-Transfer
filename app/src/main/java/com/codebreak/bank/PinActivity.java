package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eyalbira.loadingdots.LoadingDots;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PinActivity extends AppCompatActivity  implements NumPinView.KeyListener {
    NumPinView numPinView;

    LoadingDots loadingDots;
    View root;
    PinDotView pinDotView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_pin);
        root = findViewById(android.R.id.content);
        numPinView = findViewById(R.id.num_pin_view);
        pinDotView = findViewById(R.id.pin_dot_view);
        loadingDots = findViewById(R.id.loading_dots);
        loadingDots.setVisibility(View.GONE);
        numPinView.setKeyListener(this);

    }

    @Override
    public void onKeyPressed(int count) {
        if(count==4)
        {
            numPinView.setShouldDisablePin(true);
            pinDotView.setVisibility(View.GONE);
            loadingDots.setVisibility(View.VISIBLE);
            verifyPin(numPinView.getPin());

        }
        pinDotView.setFillCount(count);
    }

    private void verifyPin(final int[] pin) {
        FirebaseDatabase.getInstance().getReference().child("WUAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Snackbar.make(root,convertIntArrayToString(pin),Snackbar.LENGTH_SHORT).show();

                if(dataSnapshot.child("loginPass").getValue().toString().equals(convertIntArrayToString(pin)))
                {
                    startActivity(new Intent(PinActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    //Snackbar.make(root,"Please enter correct PIN",Snackbar.LENGTH_SHORT).show();
                    numPinView.setShouldDisablePin(false);
                    pinDotView.setVisibility(View.VISIBLE);
                    loadingDots.setVisibility(View.GONE);
                    numPinView.clearPin();
                    pinDotView.unFillAll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    String convertIntArrayToString(int[] input)
    {
        String strSeparator = "";

        String strNumbers = Arrays.toString(input);
        strNumbers = strNumbers.replaceAll(", ", strSeparator).replace("[", "").replace("]", "");
        return strNumbers;
    }
}
