package com.codebreak.bank.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppConfig extends AppCompatActivity {
    public  static String contractAddress = "0xa3b9636a6f1dc0ee75caa2563548f28ec6c9686f";
    public  static String contractABI = "[\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"recipient\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"amount1\",\n" +
            "\t\t\t\t\"type\": \"bytes32\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"amount2\",\n" +
            "\t\t\t\t\"type\": \"bytes32\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"cur1\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"cur2\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"transact\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": true,\n" +
            "\t\t\t\t\"name\": \"sender\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": true,\n" +
            "\t\t\t\t\"name\": \"receiver\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"amount1\",\n" +
            "\t\t\t\t\"type\": \"bytes32\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"amount2\",\n" +
            "\t\t\t\t\"type\": \"bytes32\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"cur1\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"cur2\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"TransactionSent\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"get\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bytes32\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"bytes4\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t}\n" +
            "]";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}