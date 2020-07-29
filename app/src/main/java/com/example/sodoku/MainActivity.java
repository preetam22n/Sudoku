package com.example.sodoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int grid, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare the interactive views
        SeekBar barLevel;
        Button btnStart, btnSolve;

        // Initialize the views
        barLevel = findViewById(R.id.barDIfficulty);
        btnStart = findViewById(R.id.btnStart);
        btnSolve = findViewById(R.id.btnSolve);
        grid = 4;
        level = 0;

        // Implement function of level selector
        barLevel.setMax(10);
        barLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                level = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // NO need
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // NO need
            }
        });

        // Start button function
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("My","Start button pressed");
            }
        });

        // Solve button function
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (grid) {
                    case 4:
                        Intent intent1 = new Intent(MainActivity.this, SolverActivity4x4.class);
                        startActivity(intent1);
                        break;
                    case 6:
                        Intent intent2 = new Intent(MainActivity.this, SolverActivity6x6.class);
                        startActivity(intent2);
                        break;
                    case 9:
                        Intent intent3 = new Intent(MainActivity.this, SolverActivity9x9.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
    }

    // Grid selector function for radio buttons
    public void gridSizeSelect(View view) {
        boolean selected = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radBtn4:
                if (selected) grid = 4;
                break;
            case R.id.radBtn6:
                if (selected) grid = 6;
                break;
            case R.id.radBtn9:
                if (selected) grid = 9;
                break;
        }
    }
}