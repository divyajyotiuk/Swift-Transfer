package com.codebreak.bank;

import android.os.Bundle;
import android.view.View;

import com.eyalbira.loadingdots.LoadingDots;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PinActivity extends AppCompatActivity  implements NumPinView.KeyListener {
    NumPinView numPinView;

    LoadingDots loadingDots;
    PinDotView pinDotView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_set_pin);
        numPinView = findViewById(R.id.num_pin_view);
        pinDotView = findViewById(R.id.pin_dot_view);
        loadingDots = findViewById(R.id.loading_dots);
        loadingDots.setVisibility(View.GONE);
        numPinView.setKeyListener(this);

    }

    @Override
    public void onKeyPressed(int count) {
        if(count==4)
        {
            numPinView.setShouldDisablePin(true);
            pinDotView.setVisibility(View.GONE);
            loadingDots.setVisibility(View.VISIBLE);

        }
        pinDotView.setFillCount(count);
    }
}
