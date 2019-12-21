package com.codebreak.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import network.pocket.core.model.Wallet;
import network.pocket.eth.PocketEth;

public class CreateWallet extends AppCompatActivity {

    PocketEth pocketEth;
    Context appContext;
    Wallet wallet;
    TextView publicView;
    TextView privateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_wallet);

        // Create wallet and continue button
        Button create_W = (Button)findViewById(R.id.generate_wallet_btn);

        // display addresses:
        publicView = (TextView)findViewById(R.id.public_key_text);
        privateView = (TextView)findViewById(R.id.private_key_text);

        // Creates a wallet
        create_W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View iw) {
                createWallet();
            }
        });

        this.appContext = CreateWallet.this;
        // Instantiate PocketAion
        List<String> netIds = new ArrayList<>();
        netIds.add(PocketEth.Networks.RINKEBY.getNetID());
        this.pocketEth = new PocketEth(this.appContext,"DEVfF1RpqCPbm1X96qDAb85", netIds,5,50000,"4");

    }

    protected void createWallet() {
        wallet = this.pocketEth.getRinkeby().createWallet();

        publicView.setText(wallet.getAddress());
        privateView.setText(wallet.getPrivateKey());

        //show dialog "Wallet created! Proceed on the dialog box - goes to next activity"
    }

    protected void proceed(){
        Intent intent = new Intent(this, MainActivity.class); //should go to set payment pin activity
        intent.putExtra("address",wallet.getAddress());
        intent.putExtra("private_key",wallet.getPrivateKey());
        startActivity(intent);
    }
}
