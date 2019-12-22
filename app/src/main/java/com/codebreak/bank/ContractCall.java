package com.codebreak.bank;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.codebreak.bank.smartcontract.SmartContract;

import java.math.BigInteger;

import androidx.appcompat.app.AppCompatActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;

public class ContractCall extends AppCompatActivity {

    Wallet wallet;
    PocketEth pocketEth;
    Context appContext;
    TextView displayKey;
    SmartContract smartContract;
    public EthContract ethContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_call);
        this.appContext =ContractCall.this;
        // Instantiate PocketAion


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // String address = extras.getString("address");
            String address = "0x53D8C4d0a0dDD9faC8f5D1ab33E8e1673d9481Da"; //display address too along with balance
            String privateKey = "198ccd740c0b57fc8bcb25d544683684aebb1425738fe580a4fa6e0d8ed85f79";
            //  String privateKey = extras.getString("privateKey");


            displayKey.setText(address);

            this.wallet = new Wallet(privateKey, address, this.pocketEth.getRinkeby().getNet().toString(), this.pocketEth.getRinkeby().getNetID().toString());

        }
        this.smartContract = new SmartContract(this.appContext, this.wallet, this.pocketEth);
        this.ethContract = smartContract.ethContract;
        //send transaction

    }
    public void toastAsync(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    public void viewEthBalance(){
        pocketEth.getRinkeby().getEth().getBalance(wallet.getAddress(),null, new Function2<PocketError, BigInteger, Unit>() {
            @Override
            public Unit invoke(PocketError pocketError, BigInteger bigInteger) {

                if (bigInteger != null) {
                    toastAsync(bigInteger.toString());
                } else {
                    toastAsync("Couldn't fetch balance at this moment");
                }
                return null;
            }
        });

    }

//    public void sendTxn(View view){
//
//        try {
//
//
//            smartContract.Transact();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}



