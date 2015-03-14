package app.chraz.chronometer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chraz on 13/03/15.
 */
public class ChronosServices extends Service {
    public ChronosServices() {
        super();
        timer = new Timer();
    }

    private Timer timer;
    static final int UPDATE_INTERVAL = 1000;
    private TimerTask timerTask;
    private DoBackgroundTask backgroundTask;

    private void doSomethingRepeatedly() {
        timerTask =  new TimerTask() {
            @Override
            public void run() {
                Log.d("Chronos Services", "Second pass");
                tic();
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, UPDATE_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
        doSomethingRepeatedly();
        return START_STICKY;
    }

    private int downloadServices(String url) {
        new DoBackgroundTask().execute();
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancel();
        Toast.makeText(getBaseContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    public void tic(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("tic");
        getBaseContext().sendBroadcast(broadcastIntent);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private class DoBackgroundTask extends AsyncTask<Void, Integer, Long> {
        boolean canceled;

        public DoBackgroundTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            Log.d("Asynk Task", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Void... params) {
            canceled = false;
            Log.d("Asynk Task", "Run in Background!");
            int totalSeconds = 10;
            int i=0;
            while(i <totalSeconds && !canceled) {
                try {
                    Thread.sleep(5000);
                    publishProgress(i);
                    Log.d("Asynk Task", "Run step");
                } catch (InterruptedException e) {
                }
                i++;
            }

            return Long.parseLong(""+i);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("Asynk Task", "Progress: " + values[0]);

        }



        @Override
        protected void onCancelled(Long aLong) {
            super.onCancelled(aLong);
            cancel();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancel();
        }
        public void cancel() {
            canceled = true;
            if(timer != null) {
                timer.cancel();
                timer.purge();
            }
        }

    }
    public void cancel() {
//        if (timerTask != null) {
//            timerTask.cancel();
//        }
        if(backgroundTask != null) {
            backgroundTask.cancel(true);
        }

        if(timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
