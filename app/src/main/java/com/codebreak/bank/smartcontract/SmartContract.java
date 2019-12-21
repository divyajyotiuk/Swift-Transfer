package com.codebreak.bank.smartcontract;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codebreak.bank.util.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import network.pocket.core.errors.PocketError;
import network.pocket.core.model.Wallet;
import network.pocket.eth.EthContract;
import network.pocket.eth.PocketEth;
import network.pocket.eth.exceptions.EthContractException;
import network.pocket.eth.util.HexStringUtil;


public class SmartContract {

    PocketEth pocketEth;
    Context appContext;
    Wallet wallet;

    public EthContract ethContract;


    public SmartContract(Context context, Wallet wallet, PocketEth pocketEth){
        this.pocketEth = pocketEth;
        this.appContext = context;
        this.wallet = wallet;
        // Setup AionContract
        String contractAddress = AppConfig.contractAddress;
        String contractABI = AppConfig.contractABI;

        try {
            JSONArray contractABIArray = new JSONArray(contractABI);
            this.ethContract = new EthContract(pocketEth.getRinkeby(),contractAddress,contractABIArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String encode(String ascii){
        // Step-1 - Convert ASCII string to char array
        char[] ch = ascii.toCharArray();

        // Step-2 Iterate over char array and cast each element to Integer.
        StringBuilder builder = new StringBuilder();

        for (char c : ch) {
            int i = (int) c;
            // Step-3 Convert integer value to hex using toHexString() method.
            builder.append(Integer.toHexString(i).toUpperCase());
        }
        return  "0x" + builder.toString();
    }

    public byte[] encodeHexToPaddedByteArray(String hex, int maxLength) {
        byte[] currBytes = HexStringUtil.hexStringToByteArray(hex);
        if (currBytes.length >= maxLength) {
            return currBytes;
        }
        byte[] result = new byte[maxLength];
        // Pad with 0 bytes
        Arrays.fill(result, (byte) 0);
        int sub = (result.length - currBytes.length);
        for (int j = sub; j < maxLength; j++) {
            result[j] = currBytes[j - sub];
        }
        return result;
    }


    public void Transact() throws JSONException {

        //take details from the transactionlist
        String recipient = "0x53D8C4d0a0dDD9faC8f5D1ab33E8e1673d9481Da";
        byte[] Send_am = encodeHexToPaddedByteArray(encode("500"), 32);
        byte[] Re_am = encodeHexToPaddedByteArray(encode("400"), 32);
        byte[] Send_cur= encodeHexToPaddedByteArray(encode("INR"), 4);
        byte[] Re_cur = encodeHexToPaddedByteArray(encode("USD"), 4);

        ArrayList<Object> functionParams = new ArrayList<>();
        functionParams.add(0,recipient);
        functionParams.add(1,Send_am);
        functionParams.add(2,Re_am);
        functionParams.add(3,Send_cur);
        functionParams.add(4,Re_cur);


        //execute contract function
        try {

            this.ethContract.executeFunction("transact", wallet, functionParams, null, new BigInteger("300000"), new BigInteger("20000000000"), new BigInteger("0"), new Function2<PocketError, String, Unit>() {
                @Override
                public Unit invoke(PocketError pocketError, String result) {
                    if (pocketError != null) {
                        pocketError.printStackTrace();
                    }else{
                        Log.d("txHash", result);

                        pocketEth.getRinkeby().getEth().getTransactionReceipt(result, new Function2<PocketError, JSONObject, Unit>() {
                            @Override
                            public Unit invoke(PocketError pocketError, JSONObject jsonObject) {
                                if(jsonObject!=null){
                                    Log.d("returned ",jsonObject.toString());
                                }else
                                {
                                    Log.d("","Couldn't fetch");
                                }
                                return null;
                            }
                        });
                        // Toast.makeText(appContext,result,Toast.LENGTH_LONG);
                    }
                    return null;
                }
            });


        } catch (EthContractException e) {
            e.printStackTrace();
        }

    }
}