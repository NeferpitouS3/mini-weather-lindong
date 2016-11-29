package com.example.neferpitou.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Neferpitou on 2016/11/21.
 */

public class MyService extends Service {

    private Timer timer = new Timer();

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder{

        public MyService getService(){
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                repeatedUpdate();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }


    private void repeatedUpdate(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("UPDATE_NOW");
                getBaseContext().sendBroadcast(broadcastIntent);
            }
        },0,60000);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();

    }
}
