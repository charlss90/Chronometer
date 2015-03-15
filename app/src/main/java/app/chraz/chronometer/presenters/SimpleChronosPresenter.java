package app.chraz.chronometer.presenters;

import app.chraz.chronometer.entities.Chronometer;
import app.chraz.chronometer.entities.ChronosPresenter;
import app.chraz.chronometer.entities.ChronosView;
import butterknife.OnItemLongClick;

/**
 * Created by chraz on 14/03/15.
 */
public class SimpleChronosPresenter implements ChronosPresenter {

    private Chronometer chronos;
    private ChronosView chronosView;

    @Override
    public void updateChronometer(int increment) {
        chronos.tic(increment);
//        chronosView.updateTime(chronos.getTime());
    }

    @Override
    public void start() {
        chronos.start();
        chronosView.start();
    }

    @Override
    public void stop() {
        chronosView.stop();
        chronos.stop();
    }

    @Override
    public void pause() {
        chronosView.pause();
    }

    public SimpleChronosPresenter(ChronosView view, Chronometer chronos) {
        super();
        this.chronosView = view;
        this.chronos = chronos;
    }
}
