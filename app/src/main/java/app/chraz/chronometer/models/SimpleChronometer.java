package app.chraz.chronometer.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import app.chraz.chronometer.entities.Chronometer;

/**
 * Created by chraz on 14/03/15.
 */
public class SimpleChronometer implements Chronometer {

    private Calendar currentTime;

    @Override
    public void stop() {
        currentTime.clear();
    }

    @Override
    public void pause() {

    }

    private SimpleDateFormat format;

    @Override
    public String getTime() {
        return format.format(currentTime.getTime());
    }

    @Override
    public void start() {
        if (currentTime == null) {
            currentTime = new GregorianCalendar();
            format = new SimpleDateFormat("mm:ss");
            currentTime.clear();
        }
    }

    @Override
    public void tic(int increment) {
        currentTime.add(Calendar.SECOND, increment);
    }

    @Override
    public void onResume() {
        //TODO: implementar

    }

    public SimpleChronometer() {
        super();
    }
}
