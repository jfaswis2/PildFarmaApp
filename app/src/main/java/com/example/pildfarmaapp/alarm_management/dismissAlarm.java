package com.example.pildfarmaapp.alarm_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;

import com.example.pildfarmaapp.R;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class dismissAlarm extends AppCompatActivity {

    Button dismissButton;
    TextClock textClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_alarm);

        dismissButton = (Button) findViewById(R.id.button_dismiss_alarm);
        textClock = (TextClock) findViewById(R.id.text_clock_dismiss);

        View layout = findViewById(R.id.dismissLayout);

        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}