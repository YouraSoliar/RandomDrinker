package com.example.randomdrinker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimerActivity extends AppCompatActivity {

    private FloatingActionButton floatingBack;
    private RelativeLayout relativeLayoutTimer;
    private ImageButton imagePlay;
    private ImageButton imageReset;
    private Chronometer chronometer;
    private TextView textViewLogs;
    private boolean isInitialSound = true;
    private int minValue;
    private int maxValue;
    private double instanceChance;
    private String soundName;
    private String randomTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        createView();
        initActions();
    }

    private void createView() {
        this.floatingBack = findViewById(R.id.floatingBack);
        this.imagePlay = findViewById(R.id.imagePlay);
        this.imageReset = findViewById(R.id.imageReset);
        this.chronometer = findViewById(R.id.chronometer);
        this.textViewLogs = findViewById(R.id.textViewLogs);
        this.relativeLayoutTimer = findViewById(R.id.relativeLayoutTimer);

        Intent intent = getIntent();
        minValue = intent.getIntExtra("minValue", 1);
        maxValue = intent.getIntExtra("maxValue", 2);
        instanceChance = intent.getDoubleExtra("instanceChance", 0.01);
        isInitialSound = intent.getBooleanExtra("isInitialSound", true);
        soundName = intent.getStringExtra("soundName");
    }

    private void initActions() {
        floatingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageReset.setVisibility(View.VISIBLE);
                imagePlay.setVisibility(View.GONE);

                if (isInitialSound) {
                    playSound();
                }

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                getRandomTimeValue();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equals(randomTime)) {

                    new Handler(Looper.getMainLooper()).post(new Runnable () {
                        @Override
                        public void run () {
                            textViewLogs.append(chronometer.getText());
                            textViewLogs.append(System.getProperty("line.separator"));
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            makeSplash();
                            playSound();
                            getRandomTimeValue();
                        }
                    });
                }
            }
        });


        imageReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageReset.setVisibility(View.GONE);
                imagePlay.setVisibility(View.VISIBLE);

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
            }
        });
    }

    public void makeSplash() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicBoolean isFlash = new AtomicBoolean(true);
        // Schedule the task to run every 200 milliseconds (5 times per second) with an initial delay of 0 milliseconds,
        // and repeat for 2 seconds (2000 milliseconds in total)
        scheduler.scheduleAtFixedRate(() -> {
            isFlash.set(!isFlash.get());

            if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                try {
                    String backCameraID = cameraManager.getCameraIdList()[0];

                    cameraManager.setTorchMode(backCameraID, isFlash.get());
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if (isFlash.get()) {
                relativeLayoutTimer.setBackgroundColor(getResources().getColor(R.color.splash));
            } else {
                relativeLayoutTimer.setBackgroundColor(getResources().getColor(R.color.dark_purple));
            }

        }, 0, 500, TimeUnit.MILLISECONDS);
        // Schedule a task to shutdown the scheduler after 2 seconds
        scheduler.schedule(() -> {
            scheduler.shutdown();
        }, 3, TimeUnit.SECONDS);

    }

    public void playSound() {
        MediaPlayer mp;
        switch (soundName) {
            case "dwayne":
                mp = MediaPlayer.create(TimerActivity.this, R.raw.sound_dwayne);
                break;
            case "rock":
                mp = MediaPlayer.create(TimerActivity.this, R.raw.sound_rock);
                break;
            case "tube":
                mp = MediaPlayer.create(TimerActivity.this, R.raw.sound_tube);
                break;
            case "fart":
                mp = MediaPlayer.create(TimerActivity.this, R.raw.sound_fart);
                break;
            default:
                mp = MediaPlayer.create(TimerActivity.this, R.raw.sound_bell);
                break;
        }
        mp.start();
    }

    public void getRandomTimeValue() {
        Random random = new Random();
        randomTime = "00:05";

        double randomValue = random.nextDouble();
        if (randomValue > instanceChance) {
            int randomMinutesValue = (int)Math.floor(Math.random() * (maxValue - minValue + 1) + minValue);

            int randomSecondsValue;
            if (randomMinutesValue == maxValue) {
                randomSecondsValue = 0;
            } else {
                randomSecondsValue= (random.nextInt(60) + 1);
            }
            randomTime = "";
            if (randomMinutesValue < 10) {
                randomTime = "0";
            }
            randomTime += randomMinutesValue + ":";

            if (randomSecondsValue < 10) {
                randomTime += "0";
            }
            randomTime += randomSecondsValue + "";
        }

        Toast.makeText(TimerActivity.this, randomTime, Toast.LENGTH_SHORT).show();

    }

}