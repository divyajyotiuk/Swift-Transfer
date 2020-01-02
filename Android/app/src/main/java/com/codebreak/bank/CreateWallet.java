package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import network.pocket.core.model.Wallet;
import network.pocket.eth.PocketEth;

public class CreateWallet extends AppCompatActivity {

    PocketEth pocketEth;
    Wallet wallet;
    TextInputEditText publicView;
    TextInputEditText privateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_wallet);

        Button proceed = findViewById(R.id.generate_wallet_btn);
        publicView =findViewById(R.id.public_key);
        privateView = findViewById(R.id.private_key);
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this,"", netIds,5,50000,"4");
        wallet = this.pocketEth.getRinkeby().createWallet();
        publicView.setText(wallet.getAddress());
        privateView.setText(wallet.getPrivateKey());
        Toast.makeText(this, "Wallet successfully created!", Toast.LENGTH_SHORT).show();
        proceed.setOnClickListener(iw -> proceed());


    }



    protected void proceed(){
        Intent intent = new Intent(this, SetPaymentPinActivity.class); //should go to set payment pin activity
        intent.putExtra("address",wallet.getAddress());
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("WUAccount")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("address")
                .setValue(wallet.getAddress());
        intent.putExtra("private_key",wallet.getPrivateKey());
        startActivity(intent);
        finish();
    }
}
