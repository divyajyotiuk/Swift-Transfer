package com.codebreak.bank;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.codebreak.bank.smartcontract.SmartContract;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView avatar, share;
    TextView name, phoneNo, userId, ether, walletBalance;
    ProgressBar progressBar;
    String id;
    private PocketEth pocketEth;
    private Wallet wallet;
    private SmartContract smartContract;
    private EthContract ethContract;
    MaterialCardView logOutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializePocket();
        viewEthBalance();
        name = findViewById(R.id.name);
        ether = findViewById(R.id.ether);
        walletBalance = findViewById(R.id.wallet_balance);
        phoneNo = findViewById(R.id.phone);
        userId = findViewById(R.id.userID);
        share = findViewById(R.id.share);
        progressBar = findViewById(R.id.progress);
        View view = findViewById(android.R.id.content);
        Window window = getWindow();
        logOutCard = findViewById(R.id.card_log_out);
        logOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i =new Intent(ProfileActivity.this, SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
avatar = findViewById(R.id.avatar);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.yellow_500));
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        share.setVisibility(View.GONE);
        share.setOnClickListener(v -> {
            if (id != null) {
                String shareBody = "This is my userId for swift transfer.\n" +id;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        FirebaseDatabase.getInstance().getReference().child("WUAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.child("fullName").getValue().toString();
                TextDrawable drawable = TextDrawable.builder().beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.SANS_SERIF)
                        .fontSize(Math.round(Math.round(getResources().getDisplayMetrics().scaledDensity * 38))) /* size in px */
                        .toUpperCase()
                        .endConfig()
                        .buildRound(fullName.split(" ")[0].substring(0,1),Color.parseColor("#0288d1"));
                avatar.setImageDrawable(drawable);
                walletBalance.setText(dataSnapshot.child("walletBalance").getValue().toString());
                name.setText(fullName);
                phoneNo.setText(dataSnapshot.child("phoneNo").getValue().toString());
                id = dataSnapshot.child("userID").getValue().toString();
                userId.setText(id);
                progressBar.setVisibility(View.GONE);
                share.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    void initializePocket()
    {
        List<String> netIds = new ArrayList<>();

        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        //you need to have pocket developer ID
        this.pocketEth = new PocketEth(this,"", netIds,5,50000,"4");

        String address = ""; //display address too along with balance
        String privateKey = "";
        this.wallet = new Wallet(privateKey, address, this.pocketEth.getRinkeby().getNet().toString(), this.pocketEth.getRinkeby().getNetID());
        this.smartContract = new SmartContract(this, this.wallet, this.pocketEth);
        this.ethContract = smartContract.ethContract;
    }

    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    public void viewEthBalance() {
        pocketEth.getRinkeby().getEth().getBalance(wallet.getAddress(), null, new Function2<PocketError, BigInteger, Unit>() {
            @Override
            public Unit invoke(PocketError pocketError, BigInteger bigInteger) {

                if (bigInteger != null) {
                    //toastAsync(bigInteger.toString());
                    runOnUiThread(()->ether.setText(bigInteger.toString()));

                } else {
                    toastAsync("Couldn't fetch balance at this moment");
                }
                return null;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

}
