package com.worldexplorer.motion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GoToRandomPositionActivity extends AppCompatActivity {

    private ImageView box;
    private TextView coordinates;
    private TextView motion;
    private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_random_position);
        coordinates = findViewById(R.id.coordinates);
        motion = findViewById(R.id.motion);
        box = findViewById(R.id.box);
        box.setScaleX(0.5f);
        box.setScaleY(0.5f);
        // on below line we are creating and initializing
        // variable for display metrics.
        DisplayMetrics displayMetrics = new DisplayMetrics();

        // on below line we are getting metrics for display using window manager.
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // on below line we are getting height
        // and width using display metrics.
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        box.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        view.setY(random.nextInt(height / 2));
                        view.setX(random.nextInt(width / 2));
                        break;
                    }
                }
                return false;
            }
        });
    }
}