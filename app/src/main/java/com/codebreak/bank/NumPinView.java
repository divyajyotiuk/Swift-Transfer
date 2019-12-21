package com.codebreak.bank;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


public class NumPinView extends ConstraintLayout implements View.OnClickListener {

    private TextView num1;
    private TextView num2;
    private TextView num3;
    private TextView num4;
    private TextView num5;
    private TextView num6;
    private TextView num7;
    private TextView num8;
    private TextView num9;
    private TextView num0;
    private ImageButton delete;
    int pin[] = new int[4];
    private boolean shouldDisablePin= false;
    private int count=0;
    private KeyListener listener;

    public NumPinView(Context context) {
        this(context, null);
    }

    public NumPinView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumPinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_pin, this, true);
        num0 = findViewById(R.id.num0);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        delete = findViewById(R.id.btn_delete);
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        delete.setOnClickListener(this);

    }


    public void setKeyListener(KeyListener listener)
    {

        this.listener = listener;
    }

    public int[] getPin() {
        return pin;
    }

    public void clearPin()
    {
        pin = new int[4];
        count=0;
    }

    @Override
    public void onClick(View v) {
        if(shouldDisablePin)
            return;
        if(v.getId()==R.id.btn_delete)
        {
            count--;
            if(count<0)
                count=0;
        }

        if(count>=4)
            return;

        switch (v.getId())
        {
            case R.id.num0:
                pin[count++]=0;

                break;
            case R.id.num1:
                pin[count++]=1;
                break;
            case R.id.num2:
                pin[count++]=2;
                break;
            case R.id.num3:
                pin[count++]=3;
                break;
            case R.id.num4:
                pin[count++]=4;
                break;
            case R.id.num5:
                pin[count++]=5;
                break;
            case R.id.num6:
                pin[count++]=6;
                break;
            case R.id.num7:
                pin[count++]=7;
                break;
            case R.id.num8:
                pin[count++]=8;
                break;
            case R.id.num9:
                pin[count++]=9;
                break;

        }
        if(listener!=null)
            listener.onKeyPressed(count);

    }

    public boolean isShouldDisablePin() {
        return shouldDisablePin;
    }

    public void setShouldDisablePin(boolean shouldDisablePin) {
        this.shouldDisablePin = shouldDisablePin;
    }


     interface KeyListener
    {
        void onKeyPressed(int count);
    }
}
