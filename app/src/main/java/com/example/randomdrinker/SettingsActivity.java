package com.example.randomdrinker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    private Button buttonToTimer;
    private Button buttonTestSound;
    private EditText editTextMin;
    private EditText editTextMax;
    private EditText editTextInstance;
    private CheckBox checkBoxInitialSound;
    private CheckBox checkBoxInitialHint;

    private int minValue;
    private int maxValue;
    private double instanceChance;
    private boolean isInitialSound;
    private boolean isHint;

    private Spinner spinnerSounds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createView();
        initActions();
    }

    private void createView() {
        this.buttonToTimer = findViewById(R.id.buttonToTimer);
        this.buttonTestSound = findViewById(R.id.buttonTestSound);
        this.editTextMin = findViewById(R.id.editTextMin);
        this.editTextMax = findViewById(R.id.editTextMax);
        this.editTextInstance = findViewById(R.id.editTextInstance);
        this.checkBoxInitialSound = findViewById(R.id.checkBoxInitialSound);
        this.checkBoxInitialHint = findViewById(R.id.checkBoxInitialHint);
        this.spinnerSounds = findViewById(R.id.spinnerSounds);
    }

    private void initActions() {

        buttonToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextMax.getText().toString().equals("") ||
                        editTextMin.getText().toString().equals("") ||
                            editTextInstance.getText().toString().equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    maxValue = Integer.parseInt(editTextMax.getText().toString());
                    minValue = Integer.parseInt(editTextMin.getText().toString());
                    instanceChance = (Integer.parseInt(editTextInstance.getText().toString())) * 0.01;
                    isInitialSound = checkBoxInitialSound.isChecked();
                    isHint = checkBoxInitialHint.isChecked();

                    String soundName = spinnerSounds.getSelectedItem().toString();

                    if (maxValue > minValue) {
                        int random = new Random().nextInt((maxValue - minValue) + 1) + minValue;
                        Intent intent = new Intent(SettingsActivity.this, TimerActivity.class);
                        intent.putExtra("minValue", minValue);
                        intent.putExtra("maxValue", maxValue);
                        intent.putExtra("instanceChance", instanceChance);
                        intent.putExtra("isInitialSound", isInitialSound);
                        intent.putExtra("isHint", isHint);
                        intent.putExtra("soundName", soundName);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SettingsActivity.this, "Incorrect min max values", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        buttonTestSound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MediaPlayer mp;
                String selectedSound = spinnerSounds.getSelectedItem().toString();
                switch (selectedSound) {
                    case "dwayne":
                        mp = MediaPlayer.create(SettingsActivity.this, R.raw.sound_dwayne);
                        break;
                    case "rock":
                        mp = MediaPlayer.create(SettingsActivity.this, R.raw.sound_rock);
                        break;
                    case "tube":
                        mp = MediaPlayer.create(SettingsActivity.this, R.raw.sound_tube);
                        break;
                    case "fart":
                        mp = MediaPlayer.create(SettingsActivity.this, R.raw.sound_fart);
                        break;
                    default:
                        mp = MediaPlayer.create(SettingsActivity.this, R.raw.sound_bell);
                        break;
                }
                mp.start();

            }
        });
    }

}