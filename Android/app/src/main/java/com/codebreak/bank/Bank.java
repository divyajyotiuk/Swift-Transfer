package com.codebreak.bank;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Bank extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:875508770867:web:0dea2fbe0bdb808c") // Required for Analytics.
                .setApiKey("AIzaSyCf1vh20ZxwAj_q-xewYVZH3Yl8w3B7xVc") // Required for Auth.
                .setDatabaseUrl("https://bankserver-dafa0.firebaseio.com") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this /* Context */, options, "secondary");

    }
}
