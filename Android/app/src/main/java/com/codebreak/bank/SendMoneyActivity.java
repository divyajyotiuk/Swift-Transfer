package com.codebreak.bank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.codebreak.bank.conversion.ForeignExchangeApi;
import com.codebreak.bank.model.ExchangeRate;
import com.codebreak.bank.model.TxnList;
import com.codebreak.bank.smartcontract.SmartContract;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;

public class SendMoneyActivity extends AppCompatActivity {

    EditTextWithPrefix etAmount;
    EditTextWithPrefix etFinalAmount;
    TextInputEditText userId;
    AutoCompleteTextView actvConversion;
    ImageView btnContacts;
    MaterialButton btnDone;
    private ArrayAdapter<String> conversionAdapter;
    DatabaseReference databaseReference;
    private Double senderBalance;
    private View root;
    ProgressDialog dialog ;
    private static final int REQUEST_PIN = 250;

    private String uid;
    private DatabaseReference txnListRef;
    private Animation rotation;
    private PocketEth pocketEth;
    private Wallet wallet;
    private SmartContract smartContract;
    private EthContract ethContract;
    private String txnKey;


    void initializePocket()
    {
        List<String> netIds = new ArrayList<>();

        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this,"DEVfF1RpqCPbm1X96qDAb85", netIds,5,50000,"4");

        String address = "0x53D8C4d0a0dDD9faC8f5D1ab33E8e1673d9481Da"; //display address too along with balance
        String privateKey = "198ccd740c0b57fc8bcb25d544683684aebb1425738fe580a4fa6e0d8ed85f79";
        this.wallet = new Wallet(privateKey, address, this.pocketEth.getRinkeby().getNet().toString(), this.pocketEth.getRinkeby().getNetID());
        this.smartContract = new SmartContract(this, this.wallet, this.pocketEth);
        this.ethContract = smartContract.ethContract;
    }

    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    SmartContract.Listener listener = new SmartContract.Listener() {
        @Override
        public void onCompleted(String result) {
            txnListRef.child(senderKy).child(txnKey).child("txnID").setValue(result);
            txnListRef.child(receiverKey).child(txnKey).child("txnID").setValue(result);
            toastAsync( result);
            dialog.dismiss();
        }

        @Override
        public void onFailed(PocketError pocketError, String initialAmount, String convertedAmount, String senderCurrency, String receiverCurrency) {
            pocketError.printStackTrace();
            //toastAsync("Unknown error occured");
            try {
                smartContract.Transact(listener,initialAmount,convertedAmount,senderCurrency,receiverCurrency);
            } catch (JSONException e) {
                e.printStackTrace();
            }        }
    };

    public void sendTxn(String initialAmount, String convertedAmount, String senderCurrency, String receiverCurrency){

        try {
            smartContract.Transact(listener,initialAmount,convertedAmount,senderCurrency,receiverCurrency);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIN && resultCode==RESULT_OK) {
            convertMoney(true);
        }
    }

ForeignExchangeApi foreignExchangeApi;
    ImageView btn_convert;
    String base, symbol;

    public void convertMoney(boolean proceedToSend)
    {
        if(userId.getText().toString().isEmpty())
            showSnackbar("Username cannot be empty");
        else {
            btn_convert.startAnimation(rotation);
            databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    base = dataSnapshot.child("currency").getValue().toString();
                    etAmount.setPrefix(base);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int flag = 0;
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                if(dataSnapshot1.child("userID").getValue().toString().equals(userId.getText().toString()))
                                {
                                    flag=1;
                                    symbol = dataSnapshot1.child("currency").getValue().toString();
                                    etFinalAmount.setPrefix(symbol);
                                    foreignExchangeApi.getExchangeRate(base, symbol, new ForeignExchangeApi.ExchangeRateListener() {
                                        @Override
                                        public void onExchangeRate(ExchangeRate exchangeRate) {
                                            double amount = Math.round((exchangeRate.getRates().get(symbol)*Double.valueOf(etAmount.getText().toString())) * 100.0) / 100.0;
                                            etFinalAmount.setText(String.valueOf(amount));
                                            btn_convert.clearAnimation();
                                            if(proceedToSend)
                                                sendMoney(amount, base);

                                        }

                                        @Override
                                        public void onFailed(String message) {
                                            showSnackbar(message);
                                            btn_convert.clearAnimation();
                                        }
                                    });
                                    break;
                                }
                            }
                            if(flag==0)
                            {
                                showSnackbar("User ID does not exist");
                                btn_convert.clearAnimation();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendMoney(double amount, String base) {
        setProgress("Checking your wallet balance");
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                senderBalance = data.child("walletBalance").getValue(Double.class);
                // senderCurrency = data.child("currency").getValue(String.class);
                String senderId = data.child("userID").getValue(String.class);
                double amountToSend =amount;
                if(senderBalance>=Double.valueOf(etAmount.getText().toString()))
                {
                    databaseReference.child(uid).child("walletBalance").setValue(Math.round((senderBalance-Double.valueOf(etAmount.getText().toString())) * 100.0) / 100.0);
                    sendMoneyToReceiver(data.child("fullName").getValue().toString(), data.getKey(), senderId, amountToSend, base);
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_send_money);
        root = findViewById(android.R.id.content);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.yellow_500));
        int flags = root.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        root.setSystemUiVisibility(flags);
        initializePocket();
        etAmount = findViewById(R.id.et_amount);
        foreignExchangeApi  = new ForeignExchangeApi();
       btn_convert = findViewById(R.id.btn_convert);
         rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_rotation);
        rotation.setRepeatCount(Animation.INFINITE);
       btn_convert.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               convertMoney(false);
       }});
        btnDone = findViewById(R.id.done);
        userId = findViewById(R.id.et_user_id);
        etFinalAmount = findViewById(R.id.et_final_amount);
        txnListRef = FirebaseDatabase.getInstance().getReference("TxnList");
        dialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WUAccount");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        uid = "g0gBLPFJLwYr4gFMeGzVEiEPEPn1";
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SendMoneyActivity.this, PaymentPinActivity.class), REQUEST_PIN);
//                convertMoney(true);
            }
        });




    }
    private String senderKy;
    private String receiverKey;
    private void sendMoneyToReceiver(final String senderName, final String senderKey, final String senderId, final Double convertedAmount, final String senderCurrency) {
        setProgress("Sending money...");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag=0;
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() )
                {
                    Object receiverId = dataSnapshot1.child("userID").getValue();
                    String currency = dataSnapshot1.child("currency").getValue().toString();

                    if(receiverId!=null && receiverId.toString().equals(userId.getText().toString()))
                    {
                        flag=1;
                        databaseReference.child(dataSnapshot1.getKey())
                                .child("walletBalance")
                                .setValue(Math.round((Double.valueOf(dataSnapshot1.child("walletBalance").getValue().toString())+convertedAmount) * 100.0) / 100.0);
                        writeTxnList(dataSnapshot1.child("fullName").getValue().toString(),
                                senderName,
                                senderId,
                                receiverId.toString() ,
                                senderKey,
                                dataSnapshot1.getKey(),
                                Double.valueOf(etAmount.getText().toString()),
                                convertedAmount,
                                senderCurrency,
                                currency);
                        senderKy = senderKey;
                        receiverKey = dataSnapshot1.getKey();
                        if(!senderCurrency.equals(currency))
                        sendTxn(etAmount.getText().toString(),String.valueOf(convertedAmount),senderCurrency,currency );
                        showSnackbar("Money sent successfully");
                        //dialog.dismiss();
                        break;
                    }
                }
                if(flag==0)
                {
                    dialog.dismiss();
                    showSnackbar("Enter a valid user ID.");
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


    public void writeTxnList(String senderName,
                             String receiverName,
                             String senderId,
                             String receiverId,
                             String senderKey,
                             String receiverKey,
                             Double initialAmount,
                             Double convertedAmount,
                             String senderCurrency,
                             String receiverCurrency) {

         txnKey = txnListRef.child(senderKey).push().getKey();
        TxnList tlist = new TxnList();
        tlist.setReceiverName(receiverName);
        tlist.setSenderName(senderName);
        tlist.setInitialAmount(initialAmount);
        tlist.setConvertedAmount(convertedAmount);
        tlist.setSenderCurrency(senderCurrency);
        tlist.setReceiverCurrency(receiverCurrency);
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
}
