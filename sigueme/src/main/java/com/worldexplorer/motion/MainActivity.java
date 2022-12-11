package com.worldexplorer.motion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView box;
    private TextView coordinates;
    private TextView motion;
    private Button nextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinates = findViewById(R.id.coordinates);
        motion = findViewById(R.id.motion);
        box = findViewById(R.id.box);
        box.setScaleX(0.5f);
        box.setScaleY(0.5f);

        nextActivity = findViewById(R.id.next);
        nextActivity.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GoToRandomPositionActivity.class);
            startActivity(intent);
        });
    }

    /**
     * The MotionEvent class contains the touch related
     * information, e.g., the number of pointers,
     * the X/Y coordinates and size and pressure of each pointer.
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:{//touch the screen
                goTo(x, y, box);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                motion.setText("you are moving!");
                goTo(x, y, box);
                break;
            }
            case MotionEvent.ACTION_UP:{
                motion.setText("Action up!");
                break;
            }
        }

        coordinates.setText("x = " + x + ";" + "y = " + y);
        return super.onTouchEvent(e);
    }

    private void goTo(float x, float y, ImageView obj) {
        float moveToX = x - obj.getWidth() / 2;
        float moveToY = y - obj.getHeight() / 2;
        float translationX = x - obj.getX() - obj.getWidth() / 2;
        float translationY = y - obj.getY() - obj.getHeight() / 2;
        obj.setTranslationX(translationX);
        obj.setTranslationY(translationY);
        obj.setX(moveToX);
        obj.setY(moveToY);
    }


}
