package app.chraz.chronometer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ChronometerActivity extends ActionBarActivity {

    private boolean isRunning;


    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    @InjectView(R.id.time)
    TextView time;

    ChronometerTask chronosTask;

    public Button getStartButton() {
        return startButton;
    }

    public void setStartButton(Button startButton) {
        this.startButton = startButton;
    }

    @InjectView(R.id.btn_start)
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        ButterKnife.inject(this);

        isRunning = false;
    }

    @OnClick(R.id.btn_start)
    public void startChronometer(View btn) {
//        Toast.makeText(this, "Clicked",Toast.LENGTH_SHORT).show();
        if (!isRunning) {
            startButton.setText("Stop");
            if (chronosTask == null)
                chronosTask = new ChronometerTask(this);
            chronosTask.execute();
            isRunning = true;
        } else {
            chronosTask.cancel(true);
            startButton.setText("Start");
            isRunning = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chronometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
