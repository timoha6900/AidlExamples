package com.example.myaidlservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ControllerActivity extends Activity {

    private Button startBtn, stopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.controller_acrivity);

        initButtons();
    }

    private void initButtons() {
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(clickListener);
        stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_btn:
                    startService(new Intent(ControllerActivity.this, AidlService.class));
                    startBtn.setEnabled(false);
                    stopBtn.setEnabled(true);
                    break;

                case R.id.stop_btn:
                    stopService(new Intent(ControllerActivity.this, AidlService.class));
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                    break;
            }
        }
    };

}
