package com.codebreak.bank;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.codebreak.bank.model.TxnList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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

public class SendMoneyActivity extends AppCompatActivity {

    TextInputEditText etAmount;
    TextInputEditText etFinalAmount;
    TextInputEditText userId;
    AutoCompleteTextView actvConversion;
    ImageView btnContacts;
    MaterialButton btnDone;
    private ArrayAdapter<String> conversionAdapter;
    DatabaseReference databaseReference;
    private Double senderBalance;
    private View root;
    ProgressDialog dialog ;


    private String uid;
    private DatabaseReference txnListRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_send_money);
        root = findViewById(android.R.id.content);
        etAmount = findViewById(R.id.et_amount);
        btnDone = findViewById(R.id.done);
        userId = findViewById(R.id.et_user_id);
        etFinalAmount = findViewById(R.id.et_final_amount);
        actvConversion = findViewById(R.id.actv_conversion);
        txnListRef = FirebaseDatabase.getInstance().getReference("TxnList");
        conversionAdapter = new ArrayAdapter<>(this, R.layout.layout_list_item, getResources().getStringArray(R.array.currency));
        actvConversion.setAdapter(conversionAdapter);
        dialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        uid = "g0gBLPFJLwYr4gFMeGzVEiEPEPn1";
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setProgress("Checking your wallet balance");
               databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot data) {
                       senderBalance = data.child("walletBalance").getValue(Double.class);
                      // senderCurrency = data.child("currency").getValue(String.class);
                       String senderId = data.child("userID").getValue(String.class);
                       double amountToSend =Double.valueOf(etAmount.getText().toString());
                       if(senderBalance>=amountToSend)
                       {
                            databaseReference.child(uid).child("walletBalance").setValue(Math.round((senderBalance-amountToSend) * 100.0) / 100.0);
                            sendMoneyToReceiver(data.child("fullName").getValue().toString(), data.getKey(), senderId, amountToSend);
                       }
                       else
                       {
                           showSnackbar("You dont have enough balance in your wallet");
                           dialog.dismiss();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       dialog.dismiss();
                       showSnackbar("Some unknown error occured");
                   }
               });
            }
        });


    }

    private void sendMoneyToReceiver(final String senderName, final String senderKey, final String senderId, final Double amount) {
        setProgress("Sending money...");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() )
                {
                    Object receiverId = dataSnapshot1.child("userID").getValue();
                    String currency = dataSnapshot1.child("currency").getValue().toString();

                    if(receiverId!=null && receiverId.toString().equals(userId.getText().toString()))
                    {

                        databaseReference.child(dataSnapshot1.getKey())
                                .child("walletBalance")
                                .setValue(Double.valueOf(dataSnapshot1.child("walletBalance").getValue().toString())
                                +Double.valueOf(etAmount.getText().toString()));
                        writeTxnList(dataSnapshot1.child("fullName").getValue().toString(),senderName, senderId, receiverId.toString() ,senderKey, dataSnapshot1.getKey(),amount,currency);
                        showSnackbar("Money sent successfully");
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                showSnackbar("Some unknown error occured");
            }
        });

    }

    private void setProgress(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    void showSnackbar(String message)
    {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }


    public void writeTxnList(String senderName, String receiverName, String senderId, String receiverId, String senderKey, String receiverKey,  Double amount, String receiverCurrency) {

        String txnKey = txnListRef.child(senderKey).push().getKey();
        TxnList tlist = new TxnList();
        tlist.setReceiverName(receiverName);
        tlist.setSenderName(senderName);
        tlist.setAmount(amount);
        tlist.setCurrency(receiverCurrency);
        tlist.setFrom(senderId);
        tlist.setTo(receiverId);
        tlist.setTxnID(txnKey);
        tlist.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        tlist.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        tlist.setMoneyAdded(false);
        txnListRef.child(senderKey).child(txnKey).setValue(tlist);
        tlist.setMoneyAdded(true);
        txnListRef.child(receiverKey).child(txnKey).setValue(tlist);
//        Map<String, Object> txn = tlist.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/" + txnKey, txn);
//
//        txnListRef.updateChildren(childUpdates);


    }
}
