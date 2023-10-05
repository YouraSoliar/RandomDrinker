package com.example.randomdrinker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private TextView textViewTimer;

    private boolean isInitialSound = true;
    private int minValue;
    private int maxValue;
    private double instanceChance;
    private String soundName;


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
        this.textViewTimer = findViewById(R.id.textViewTimer);
        this.relativeLayoutTimer = findViewById(R.id.relativeLayoutTimer);

        Intent intent = getIntent();
        minValue = intent.getIntExtra("minValue", 1);
        maxValue = intent.getIntExtra("maxValue", 2);
        instanceChance = intent.getDoubleExtra("instanceChance", 0.01);
        isInitialSound = intent.getBooleanExtra("isInitialSound", true);
        soundName = intent.getStringExtra("soundName");
    }

    private void initActions() {
        getRandomSecondsValue();
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
                makeSplash();
                if (isInitialSound) {
                    playSound();
                }
            }
        });

        imageReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageReset.setVisibility(View.GONE);
                imagePlay.setVisibility(View.VISIBLE);
            }
        });
    }

    public void startTimer(boolean isAfterDrink) {

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

    public int getRandomSecondsValue() {
        Random random = new Random();
        int randomSeconds = 5;

        double randomValue = random.nextDouble();
        if (randomValue > instanceChance) {
            int randomMinutesValue = (int)Math.floor(Math.random() * (maxValue - minValue + 1) + minValue);

            randomSeconds = (random.nextInt(60) + 1) + (randomMinutesValue * 60);
        }

        return randomSeconds;
    }

}