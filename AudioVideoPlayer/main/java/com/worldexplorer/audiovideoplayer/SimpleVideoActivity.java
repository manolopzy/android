package com.worldexplorer.audiovideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SimpleVideoActivity extends AppCompatActivity implements Runnable, MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;
    SeekBar progressBar;

    FloatingActionButton play;
    FloatingActionButton stop;
    FloatingActionButton pause;
    //show how many seconds has played
    TextView progressHint;
    TextView mediaName;

    VideoView videoView;

    private boolean wasPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_video);

        videoView = (VideoView) findViewById(R.id.video_view);

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });

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
    }



    public void setupPlayer() {
        try {
            //load the song from assets folder
            mediaPlayer = MediaPlayer.create(this, R.raw.la_gallina_turuleca_video);
            mediaName.setText("la gallina turuleca");
            //mediaPlayer.prepare();
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



    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    private void play() {
        if(!wasPlaying){
            mediaPlayer.setDisplay(videoView.getHolder());
        }

        mediaPlayer.start();
        new Thread(this).start();
        play.setEnabled(false);
        pause.setEnabled(true);
        stop.setEnabled(true);
    }

    private void stop() {

        wasPlaying = true;
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
        pause.setEnabled(false);
        stop.setEnabled(false);
        findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
//                    mediaPlayer.prepare();
//                    mediaPlayer.seekTo(0);
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
        play.setEnabled(true);
        pause.setEnabled(false);
        stop.setEnabled(true);

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
}