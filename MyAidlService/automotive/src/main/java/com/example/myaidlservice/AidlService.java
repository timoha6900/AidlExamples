package com.example.myaidlservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AidlService extends Service {

    private final String[] wheather = new String[] {
            "Sunny", "Cloudy", "Rain", "Downpour", "Show", "Storm"
    };

    final RemoteCallbackList<IAidlServiceCallback> mCallbacks
            = new RemoteCallbackList<IAidlServiceCallback>();

    NotificationManager mNM;

    private static final int REPORT_MSG = 1;

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotification();

        mHandler.sendEmptyMessage(REPORT_MSG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AidlService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(R.string.service_started);

        Toast.makeText(this, R.string.service_stopped, Toast.LENGTH_LONG).show();

        mCallbacks.kill();

        mHandler.removeMessages(REPORT_MSG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IAidlService.Stub mBinder = new IAidlService.Stub() {
        public void registerCallback(IAidlServiceCallback cb) {
            if(cb != null) mCallbacks.register(cb);
        }

        public void unregisterCallback(IAidlServiceCallback cb) {
            if(cb != null) mCallbacks.unregister(cb);
        }
    };

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG).show();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPORT_MSG:
                    int temperature = (int)(Math.random()*80 - 40);
                    String wheath = wheather[(int)(Math.random()*6)];



                    final int N = mCallbacks.beginBroadcast();
                    for(int i = 0; i < N; i++) {
                        try{
                            mCallbacks.getBroadcastItem(i).valueChanged(wheath, temperature);
                        } catch (RemoteException e) {
                            //NOP
                        }
                    }
                    mCallbacks.finishBroadcast();

                    sendMessageDelayed(obtainMessage(REPORT_MSG), 1*1500);

                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void showNotification() {
        CharSequence text = getText(R.string.service_started);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ControllerActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();

        mNM.notify(R.string.service_started, notification);
    }

}
