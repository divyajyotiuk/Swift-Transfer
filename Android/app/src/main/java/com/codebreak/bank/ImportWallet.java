package com.codebreak.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ImportWallet extends AppCompatActivity {

    Button import_w_btn;
    TextView create_w_btn;
    EditText address;
    EditText private_key;
    String addr;
    String p_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_wallet);

        import_w_btn = (Button)findViewById(R.id.import_wallet_btn);
        create_w_btn = (TextView)findViewById(R.id.create_wallet);

        address = (EditText)findViewById(R.id.address);
        private_key = (EditText)findViewById(R.id.private_key);
        addr = address.getText().toString();
        p_key = private_key.getText().toString();


    }

    public void clickImport(View view){
        Intent intent = new Intent(this, MainActivity.class); //should go to set payment pin activity
        intent.putExtra("address",addr);
        intent.putExtra("private_key",p_key);
        startActivity(intent);
    }

    public void clickCreate(View view){
        Intent intent = new Intent(this, CreateWallet.class);
        startActivity(intent);
    }
}
