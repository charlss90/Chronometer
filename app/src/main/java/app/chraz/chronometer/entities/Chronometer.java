package app.chraz.chronometer.entities;

/**
 * Created by chraz on 14/03/15.
 */
public interface Chronometer extends BaseChronometer {

    void tic(int increment);

    void onResume();

    String getTime();

}
