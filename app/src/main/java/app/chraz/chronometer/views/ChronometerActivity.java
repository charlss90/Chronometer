package app.chraz.chronometer.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Calendar;
import java.util.GregorianCalendar;

import app.chraz.chronometer.ChronosServices;
import app.chraz.chronometer.R;
import app.chraz.chronometer.entities.ChronosPresenter;
import app.chraz.chronometer.entities.ChronosView;
import app.chraz.chronometer.models.SimpleChronometer;
import app.chraz.chronometer.presenters.SimpleChronosPresenter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;


public class ChronometerActivity extends ActionBarActivity implements ChronosView{

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
        setContentView(R.layout.activity_chronometer);
        ButterKnife.inject(this);
        chronosPresenter =  new SimpleChronosPresenter(this, new SimpleChronometer());
        increment = 1;
        isRunning = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(intentReciver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        intentFilter = new IntentFilter();
        intentFilter.addAction("tic");
        registerReceiver(intentReciver, intentFilter);
    }

    @OnClick(R.id.btn_start)
    public void startChronometer(View btn) {
        if (!isRunning) {
            chronosPresenter.start();
        } else {
            chronosPresenter.stop();
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
    private Calendar calendar;

    private BroadcastReceiver intentReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("tic")) {
                chronosPresenter.updateChronometer(increment);
            }
        }
    };

    @Override
    public void start() {
        controlButton.setText("Stop");
        startService(new Intent(getBaseContext(), ChronosServices.class));
        isRunning = true;
    }

    public void stopChronosService() {
        stopService(new Intent(getBaseContext(), ChronosServices.class));
        isRunning = false;
    }

    @Override
    public void pause() {
        controlButton.setText("Pause");
        stopChronosService();
    }


    @Override
    public void stop() {
        controlButton.setText("Start");
        chronosPresenter.stop();
        stopChronosService();
    }

    @OnItemLongClick(R.id.btn_start)
    public boolean stopButton(View v) {
        chronosPresenter.stop();
        return true;
    }

    @Override
    public void updateTime(String time) {
        this.time.setText(time);
    }
}
