package com.example.applicationb;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static final int MILLIS_PER_SECOND = 1000;
    private static final int SECONDS_TO_COUNTDOWN = 10;
    private TextView countdownDisplay;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        Toast.makeText(this, "The application is not an independent", Toast.LENGTH_LONG).show();

        countdownDisplay = (TextView) findViewById(R.id.time_display_box);
        showTimer(SECONDS_TO_COUNTDOWN * MILLIS_PER_SECOND);
    }


    private void showTimer(int countdownMillis) {
        if(timer != null) { timer.cancel(); }
        timer = new CountDownTimer(countdownMillis, MILLIS_PER_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownDisplay.setText("The application will be closed in "
                        + millisUntilFinished / MILLIS_PER_SECOND + " seconds");
            }
            @Override
            public void onFinish() {
                System.exit(0);
            }
        }.start();
    }
}
