package com.worldexplorer.audiovideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * {@link MediaPlayer} class is used for playing Audio and Video files.
 * Some common methods that we are going to use are:
 * start()
 * stop()
 * release() - To prevent memory leaks.
 * seekTo(position) - Jump to the play position we want
 * isPlaying()
 * getDuration() - Get the total duration of the media file in milli seconds
 * setDataSource(FileDescriptor fd) - Set the media file under the "assets" folder to be played.
 * setVolume(float leftVolume, float rightVolume) - Set the volume level. The value is between 0 and 1.
 */
public class MusicActivity extends AppCompatActivity implements Runnable, MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;
    SeekBar progressBar;

    FloatingActionButton play;
    FloatingActionButton stop;
    FloatingActionButton pause;
    //show how many seconds has played
    TextView progressHint;
    TextView mediaName;
    private Button nextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        mediaName = findViewById(R.id.media_name);
        progressHint = findViewById(R.id.progress_hint);
        //Initialize the references of the button controls
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        play.setOnClickListener(v -> {
                play();
            }
        );
        pause.setOnClickListener(v -> {
                    pause();
                }
        );
        stop.setOnClickListener(v -> {
                    stop();
                }
        );

        progressBar = findViewById(R.id.progress_bar);

        setProgressBarListener();

        setupPlayer();

        nextActivity = findViewById(R.id.next);
        nextActivity.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SimpleVideoActivity.class);
            startActivity(intent);
        });
    }



    public void setupPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            //load the song from assets folder
            AssetFileDescriptor descriptor = getAssets().openFd("la_gallina_turuleca.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaName.setText("la gallina turuleca");
            mediaPlayer.prepare();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setLooping(false);
            progressBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.setOnCompletionListener(this);
            Log.i("Music player", "the song's duration = " + mediaPlayer.getDuration());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * We update the progress bar every one second here
     */
    public void run() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        int total = mediaPlayer.getDuration();

        while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }
            progressBar.setProgress(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }


    private void setProgressBarListener() {
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                Log.i("Music activity", "the progress number = " + progress);
                int x = (int) Math.ceil(progress / 1000f);
                Log.i("Music activity", "the progress number = " + x);
                progressHint.setText(x + "s");
//                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//                    int left = (int) Math.floor((mediaPlayer.getDuration() - progress) / 1000f);
//                    if(left == 0){
//                        clearMediaPlayer();
//                        //play.setImageDrawable(ContextCompat.getDrawable(MusicActivity.this, android.R.drawable.ic_media_play));
//                        progressBar.setProgress(0);
//                    }
//                }
            }

            /**
             * When the user click the progress bar to jump to a position,
             * we update the player correspondingly
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });
    }
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    private void play() {
        mediaPlayer.start();
        new Thread(this).start();
        //hide the play button and show the rest
//        play.setVisibility(View.INVISIBLE);
//        pause.setVisibility(View.VISIBLE);
//        stop.setVisibility(View.VISIBLE);
//        play.setClickable(false);
//        pause.setClickable(true);
//        stop.setClickable(true);
        play.setEnabled(false);
        pause.setEnabled(true);
        stop.setEnabled(true);
    }

    private void stop() {
        mediaPlayer.stop();
//        pause.setVisibility(View.INVISIBLE);
//        stop.setVisibility(View.INVISIBLE);
//        pause.setClickable(false);
//        stop.setClickable(false);
        pause.setEnabled(false);
        stop.setEnabled(false);
        findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                    //play.setVisibility(View.VISIBLE);
                    //play.setClickable(true);
                    play.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    private void pause() {
        mediaPlayer.pause();

//        play.setVisibility(View.VISIBLE);
//        pause.setVisibility(View.INVISIBLE);
//        stop.setVisibility(View.VISIBLE);
//        play.setClickable(true);
//        pause.setClickable(false);
//        stop.setClickable(true);
        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(true);

    }
}