package com.worldexplorer.audiovideoplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Ctrl+alt+l to format
 * alt+shift+enter to import classes
 */
public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private MenuItem play;
    private MenuItem pause;
    private MenuItem stop;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.la_gallina_turuleca);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        play = menu.findItem(R.id.play);
        pause = menu.findItem(R.id.pause);
        stop = menu.findItem(R.id.stop);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play:
                play();
                return (true);
            case R.id.pause:
                pause();
                return (true);
            case R.id.stop:
                stop();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    private void play() {
        mediaPlayer.start();
        //hide the play button and show the rest
        play.setVisible(false);
        pause.setVisible(true);
        stop.setVisible(true);
    }

    private void stop() {
        mediaPlayer.stop();
        pause.setVisible(false);
        stop.setVisible(false);

        findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                    play.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    private void pause() {
        mediaPlayer.pause();

        play.setVisible(true);
        pause.setVisible(false);
        stop.setVisible(true);
    }

}