package app.chraz.chronometer.services;

import android.content.IntentFilter;

import app.chraz.chronometer.entities.ChronosControl;

/**
 * Created by chraz on 15/03/15.
 */
public class ChronosFilter extends IntentFilter {

    public static ChronosFilter getInstance() {
        return new ChronosFilter();
    }

    public ChronosFilter() {
        super();

        addAction(ChronosControl.PLAY);
        addAction(ChronosControl.PAUSE);
        addAction(ChronosControl.STOP);
    }
}
