package com.codebreak.bank;

import android.animation.Animator;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codebreak.bank.model.TxnList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class AddMoneyActivity extends AppCompatActivity {



    private FirebaseDatabase secondaryDatabase;
    private DatabaseReference primaryRef;
    private DatabaseReference secondaryRef;


    TextInputEditText amount;
    ViewGroup progressParent;
    ProgressBar progressBar;
    MaterialButton btnDone;
    ViewGroup root;
    AutoCompleteTextView from;
    TextView fromText;
    private ArrayAdapter<String> bankAdapter;
    private int selectedItem;
    private DatabaseReference txnListRef;


    public void writeTxnList(String senderName,
                             String receiverName,
                             String senderId,
                             String receiverId,
                             String senderKey,
                             String receiverKey,
                             Double initialAmount,
                             String senderCurrency) {

        String txnKey = txnListRef.child(senderKey).push().getKey();
        TxnList tlist = new TxnList();
        tlist.setReceiverName(receiverName);
        tlist.setSenderName(senderName);
        tlist.setInitialAmount(initialAmount);
        tlist.setConvertedAmount(initialAmount);
        tlist.setSenderCurrency(senderCurrency);
        tlist.setReceiverCurrency(senderCurrency);
        tlist.setFrom(senderId);
        tlist.setTo(receiverId);
        tlist.setTxnID(txnKey);
        tlist.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        tlist.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tlist.setMoneyAdded(false);
        txnListRef.child(senderKey).child(txnKey).setValue(tlist);
        tlist.setMoneyAdded(true);
        txnListRef.child(receiverKey).child(txnKey).setValue(tlist);



    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        final Fade fade = new Fade();

        txnListRef = FirebaseDatabase.getInstance().getReference("TxnList");
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        // Get the database for the other app.
        secondaryDatabase = FirebaseDatabase.getInstance(app);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        secondaryRef = secondaryDatabase.getReference("accounts");
        primaryRef = FirebaseDatabase.getInstance().getReference().child("WUAccount").child(uid);
        fade.setDuration(500L);
        fade.setInterpolator(new FastOutSlowInInterpolator());
        amount = findViewById(R.id.et_enter_amount);
        root = findViewById(android.R.id.content);
        progressParent = findViewById(R.id.progress_root);
        fromText = findViewById(R.id.tv_send_money_where);
        progressBar = findViewById(R.id.progress);
        btnDone = findViewById(R.id.btn_done);
        from = findViewById(R.id.actv_bank);
        bankAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.add_money));
        from.setAdapter(bankAdapter);
        from.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                selectedItem = position;
                fromText.animate().alpha(0f).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fromText.setText(bankAdapter.getItem(position));
                        fromText.animate().alpha(1f).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        });



        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(progressParent);
                progressBar.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.INVISIBLE);
                if(selectedItem==0)
                {

                    secondaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int flag = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.child("phoneNo").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                                {
                                    Double bankBalance = Double.valueOf(dataSnapshot1.child("balance").getValue().toString());
                                            final Double enteredAmount = Double.valueOf(amount.getText().toString());
                                    if(bankBalance>=enteredAmount) {
                                        secondaryRef.child(dataSnapshot1.getKey()).child("balance").setValue(Math.round((bankBalance-enteredAmount) * 100.0) / 100.0);
                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
                                        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Double walletBalance  = Double.valueOf(dataSnapshot.child("walletBalance").getValue().toString());
                                                String currency = dataSnapshot.child("currency").getValue().toString();
                                                databaseReference.child(uid).child("walletBalance").setValue(walletBalance+enteredAmount);
                                                writeTxnList(
                                                        dataSnapshot.child("fullName").getValue().toString(),
                                                        "From bank",
                                                        "bank",
                                                        dataSnapshot.child("userID").getValue().toString(),
                                                        "bank", dataSnapshot.getKey(),
                                                        enteredAmount,
                                                        currency);
                                                showSnackbar("Money added successfully");
                                                hideProgress();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                hideProgress();
                                            }
                                        });
                                    }
                                    else {
                                        showSnackbar("You do not have enough money in your bank account.");
                                        hideProgress();
                                    }
                                    break;

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else {
                        primaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                Double walletBalance  = Double.valueOf(dataSnapshott.child("walletBalance").getValue().toString());
                                final Double enteredAmount = Double.valueOf(amount.getText().toString());
                                if(walletBalance>=enteredAmount)
                                {
                                    primaryRef.child("walletBalance").setValue(walletBalance-enteredAmount);
                                    secondaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                            {
                                                if(dataSnapshot1.child("phoneNo").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
                                                {
                                                    Double bankBalance = Double.valueOf(dataSnapshot1.child("balance").getValue().toString());
                                                    secondaryRef.child(dataSnapshot1.getKey()).child("balance").setValue(Math.round((bankBalance+enteredAmount) * 100.0) / 100.0);
                                                   String currency = dataSnapshott.child("currency").getValue().toString();
                                                    writeTxnList("To bank",
                                                            dataSnapshott.child("fullName").getValue().toString(),
                                                            dataSnapshott.child("userID").getValue().toString(),
                                                            "bank",
                                                            dataSnapshott.getKey(),
                                                            "bank",
                                                            enteredAmount,
                                                            currency

                                                            );
                                                    showSnackbar("Money transferred to bank account successfully");
                                                    hideProgress();

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            showSnackbar("Some unknown error occured");

                                            hideProgress();

                                        }
                                    });

                                }
                                else
                                {
                                    showSnackbar("You do not have enough money in your wallet.");
                                    hideProgress();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }
            }
        });

    }

    private void hideProgress() {
        TransitionManager.beginDelayedTransition(progressParent);
        progressBar.setVisibility(View.GONE);
        btnDone.setVisibility(View.VISIBLE);
    }

    void showSnackbar(String msg)
    {
        Snackbar.make(root, msg, Snackbar.LENGTH_SHORT).show();

    }
}
