package app.chraz.chronometer.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import app.chraz.chronometer.services.ChronosServices;
import app.chraz.chronometer.R;
import app.chraz.chronometer.entities.ChronosPresenter;
import app.chraz.chronometer.entities.ChronosView;
import app.chraz.chronometer.models.SimpleChronometer;
import app.chraz.chronometer.presenters.SimpleChronosPresenter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ChronometerActivity extends ActionBarActivity implements ChronosView {

    private boolean isRunning;

    ChronosPresenter chronosPresenter;

    IntentFilter intentFilter;

    @InjectView(R.id.time)
    TextView time;

    @InjectView(R.id.btn_start)
    Button controlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chronometer);
        ButterKnife.inject(this);
        chronosPresenter =  new SimpleChronosPresenter(this, new SimpleChronometer());
        increment = 1;
        isRunning = false;

        displayStop(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(chronosReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        intentFilter = new IntentFilter();
        intentFilter.addAction(ChronosServices.INTERVAL_UPDATE_ACTION);
        registerReceiver(chronosReceiver, intentFilter);
    }

    @OnClick(R.id.btn_start)
    public void startChronometer(View btn) {
        if (!isRunning) {
            chronosPresenter.start();
        } else {
            chronosPresenter.pause();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chronometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private int increment;

    private BroadcastReceiver chronosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ChronosServices.INTERVAL_UPDATE_ACTION)) {
                updateTime(intent.getStringExtra(ChronosServices.UPDATE_TIME));
                chronosPresenter.updateChronometer(increment);
            }
        }
    };

    @Override
    public void start() {
        startService(new Intent(getBaseContext(), ChronosServices.class));
        controlButton.setText(R.string.pause);
//        Intent intent = new Intent();
//        intent.setAction(ChronosServices.PLAY);
//        sendBroadcast(intent);
        displayStop(true);
        isRunning = true;
    }

    public void stopChronosService() {
        stopService(new Intent(getBaseContext(), ChronosServices.class));
        isRunning = false;
    }

    public void pauseChronosService() {
        isRunning = false;
        Intent intent = new Intent();
        intent.setAction(ChronosServices.PAUSE);
        sendBroadcast(intent);
    }

    @Override
    public void pause() {
        controlButton.setText(R.string.start);
//        stopChronosService();
        pauseChronosService();
    }


    @Override
    public void stop() {
        controlButton.setText(R.string.start);
        time.setText(R.string.timer_clear);

        stopChronosService();
    }

    @InjectView(R.id.btn_stop)
    Button stopButton;

    @OnClick(R.id.btn_stop)
    public void stopButton(View v) {
        chronosPresenter.stop();
        displayStop(false);
    }

    public void displayStop(boolean isDisplay) {
        if (isDisplay) {
            stopButton.setVisibility(View.VISIBLE);
        } else {
            stopButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateTime(String time) {
        this.time.setText(time);
    }
}
