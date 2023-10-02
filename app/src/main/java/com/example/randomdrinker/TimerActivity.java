package com.example.randomdrinker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class TimerActivity extends AppCompatActivity {

    private FloatingActionButton floatingBack;
    private FloatingActionButton floatingPlay;
    private FloatingActionButton floatingReset;
    private TextView textViewTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        createView();
        initActions();
    }

    private void createView() {
        this.floatingBack = findViewById(R.id.floatingBack);
        this.floatingBack = findViewById(R.id.floatingBack);
        this.floatingBack = findViewById(R.id.floatingBack);
        this.textViewTimer = findViewById(R.id.textViewTimer);
    }

    private void initActions() {
        floatingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}