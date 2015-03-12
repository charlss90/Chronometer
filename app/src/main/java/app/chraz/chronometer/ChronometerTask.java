package app.chraz.chronometer;

import android.app.Activity;
import android.os.AsyncTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chraz on 12/03/15.
 */
public class ChronometerTask extends AsyncTask<Void, Integer, Integer> {

    private Activity chronosView;
    private Calendar calendar;
    private int increment;

    public ChronometerTask(ChronometerActivity chronosActivity) {
        chronosView = chronosActivity;
        calendar = GregorianCalendar.getInstance();
        calendar.clear();
        increment = 1;
    }

    @Override
    protected void onProgressUpdate(Integer... timer) {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        calendar.add(Calendar.SECOND, increment);
        String strTime = df.format(calendar.getTime());
        ((ChronometerActivity)chronosView).getTime().setText(strTime);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int timer = 60;
        int i;
        for(i = 0; i < 60; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(i);
        }
        return i;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        cancelled();
    }

    private void cancelled() {
        ((ChronometerActivity)chronosView).getStartButton().setText("Start");
    }

    @Override
    protected void onCancelled() {
        cancelled();
    }
}
