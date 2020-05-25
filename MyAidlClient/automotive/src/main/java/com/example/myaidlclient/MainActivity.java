package com.example.myaidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myaidlservice.IAidlService;
import com.example.myaidlservice.IAidlServiceCallback;
import com.example.aidlnativeservice.IAidlNativeService;

public class MainActivity extends Activity {

    IAidlService mService = null;
    IAidlNativeService mNativeService = null;

    private Button bindBtn, unbindBtn, bindNativeBtn, unbindNativeBtn;
    private TextView dataTV, nativeDataTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        initViews();


    }

    private void initViews() {
        bindBtn = findViewById(R.id.bind_btn);
        unbindBtn = findViewById(R.id.unbind_btn);
        bindNativeBtn = findViewById(R.id.bind_native_btn);
        unbindNativeBtn = findViewById(R.id.unbind_native_btn);
        dataTV = findViewById(R.id.data_tv);
        nativeDataTV = findViewById(R.id.native_data_tv);

        bindBtn.setOnClickListener(mClickListener);
        unbindBtn.setOnClickListener(mClickListener);
        bindNativeBtn.setOnClickListener(mClickListener);
        unbindNativeBtn.setOnClickListener(mClickListener);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IAidlService.Stub.asInterface(service);
            changeViewsState(true);

            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) { /*NOP*/ }

            Toast.makeText(MainActivity.this, R.string.connected, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            changeViewsState(false);

            Toast.makeText(MainActivity.this, R.string.disconnected, Toast.LENGTH_SHORT).show();
        }
    };

    private IAidlServiceCallback mCallback = new IAidlServiceCallback.Stub() {
        public void valueChanged(String wheather, int temperature) {
            mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, temperature, 0, wheather));
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bind_btn:
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.myaidlservice", "com.example.myaidlservice.AidlService"));
                    intent.setAction(IAidlService.class.getName());
                    bindService(intent, mConnection, 0);
                    break;

                case R.id.unbind_btn:
                    if(mService != null) {
                        try {
                            mService.unregisterCallback(mCallback);
                        } catch (RemoteException e) {
                            //NOP
                        }
                    }

                    unbindService(mConnection);
                    changeViewsState(false);
                    break;

                case R.id.bind_native_btn:
                    if(bindNativeService()) {
                        changeNativeViewsState(true);
                    }
                    break;

                case R.id.unbind_native_btn:
                    unbindNativeService();
                    changeNativeViewsState(false);
                    break;
            }
        }
    };

    private void changeViewsState(boolean isBound) {
        bindBtn.setEnabled(!isBound);
        unbindBtn.setEnabled(isBound);
        if(!isBound) {
            mService = null;
            dataTV.setText(R.string.null_data);
        }
    }

    private void changeNativeViewsState(boolean isBound) {
        bindNativeBtn.setEnabled(!isBound);
        unbindNativeBtn.setEnabled(isBound);
        if(!isBound) {
            mNativeService = null;
            nativeDataTV.setText(R.string.null_data);
        }
    }

    private boolean bindNativeService() {
        boolean result = false;
        IBinder b = ServiceManager.getService("com.example.aidlnativeservice");
        if(b != null) {
            mNativeService = IAidlNativeService.Stub.asInterface(b);
            if(mNativeService != null) {
                Log.d("AidlClient", "bindNativeService is successful");
                mHandler.sendEmptyMessage(NATIVE_MSG);
                result = true;
            } else {
                Log.d("AidlClient", "MainActivity asInterface fail.");
            }
        } else {
            Log.d("AidlClient", "MainActivity get native service fail.");
        }
        return result;
    }

    private void unbindNativeService() {
        mNativeService = null;
        mHandler.removeMessages(NATIVE_MSG);
    }

    private static final int BUMP_MSG = 1;
    private static final int NATIVE_MSG = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BUMP_MSG:
                    dataTV.setText("Weather: " + String.valueOf(msg.obj) + ", temperature: " + msg.arg1);
                    break;

                case NATIVE_MSG:
                    try {
                        String weather = mNativeService.GetWeather();
                        int temperature = mNativeService.GetTemperature();
                        nativeDataTV.setText("Wheather: " + weather + ", temperature: " + temperature);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    sendMessageDelayed(obtainMessage(NATIVE_MSG), 1*1500);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    };
}
