package com.example.simple_stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    MaterialButton reset,start,pause;
    int sec,min,milli;
    long millis,startTime,timeBuff,updateTime = 0L;
    Handler handler;
    private final Runnable runnable = new Runnable(){
        @Override
        public void run(){
            millis = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millis;
            sec = (int) (updateTime/1000);
            min = sec/ 60;
            sec=sec % 60;
            milli= (int) (updateTime % 1000);

            textView.setText(MessageFormat.format("{0}:{1}:{2}",min,String.format(Locale.getDefault(),"%02d",sec),String.format(Locale.getDefault(),"%02d",milli)));
            handler.postDelayed(this,0);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.textView);
        reset = findViewById(R.id.reset);
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);

        handler = new Handler(Looper.getMainLooper());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable,0);
                reset.setEnabled(false);
                pause.setEnabled(true);
                start.setEnabled(false);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBuff += millis;
                handler.removeCallbacks(runnable);
                reset.setEnabled(true);
                pause.setEnabled(false);
                start.setEnabled(true);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                millis=0L;
                startTime=0L;
                timeBuff=0L;
                updateTime=0L;
                sec=0;
                min=0;
                milli=0;
                textView.setText("00:00:00");
            }
        });

        textView.setText("00:00:00");
    }
}