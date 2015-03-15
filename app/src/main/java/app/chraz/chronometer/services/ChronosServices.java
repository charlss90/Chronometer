package app.chraz.chronometer.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import app.chraz.chronometer.entities.Chronometer;
import app.chraz.chronometer.models.SimpleChronometer;

/**
 * Created by chraz on 13/03/15.
 */
public class ChronosServices extends Service {

    public static final int INTERVAL_UPDATE = 1000;
    public static final String INTERVAL_UPDATE_ACTION = "notifyTic";
    public static final int INCREMENT = 1;
    
    public static final String UPDATE_TIME = "Time";
    public static final String PAUSE = "Pause";
    public static final String STOP = "Stop";
    public static final String PLAY = "Play";

    private Timer timer;
    private Intent notifyTic;
    private Chronometer chronos;

    public ChronosServices() {
        super();
        timer = new Timer();
        notifyTic = new Intent();
        notifyTic.setAction(INTERVAL_UPDATE_ACTION);
        chronos = new SimpleChronometer();
        chronos.start();
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            chronos.tic(INCREMENT);
            notifyTic.putExtra(UPDATE_TIME, chronos.getTime());
            notifyTic(notifyTic);
        }
    };

    private void startTic() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                chronos.tic(INCREMENT);
                notifyTic.putExtra(UPDATE_TIME, chronos.getTime());
                notifyTic(notifyTic);
            }
        }, 0, INTERVAL_UPDATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction(PAUSE);
        intentFilter.addAction(PLAY);
        intentFilter.addAction(STOP);

        Log.d("Chronometer Activity", "OnCreate Services");

        registerReceiver(chronosServicesReciver, intentFilter);
    }

    IntentFilter intentFilter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTic();
        Log.d("Chronometer Services", "OnStartCommand Services");

        return START_STICKY;
    }

    public void notifyTic(Intent broadcastIntent) {
        getBaseContext().sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chronosServicesReciver);
        cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void cancel() {
        if(timer != null) {
            timer.cancel();
            timer.purge();
        }

    }


    public BroadcastReceiver chronosServicesReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case PAUSE:
                    cancel();
                    break;
                case PLAY:
                    startTic();
                    break;
                case STOP:
                    stopSelf();
                    break;
            }
        }
    };

}
