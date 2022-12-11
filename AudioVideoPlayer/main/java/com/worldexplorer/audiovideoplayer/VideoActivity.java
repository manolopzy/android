package com.worldexplorer.audiovideoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoActivity extends AbstractPermissionActivity {

    private VideoView video;
    private MediaController ctlr;

    @Override
    protected String[] getDesiredPermissions() {
        return (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    @Override
    protected void onPermissionDenied() {
        Toast
                .makeText(this, R.string.msg_sorry, Toast.LENGTH_LONG)
                .show();
        finish();
    }

    @Override
    public void onReady(Bundle state) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_video);

        File clip = new File(Environment.getExternalStorageDirectory(),
                "test.mp4");

        if (clip.exists()) {
            video = (VideoView) findViewById(R.id.videoView);
            video.setVideoPath(clip.getAbsolutePath());

            ctlr = new MediaController(this);
            ctlr.setMediaPlayer(video);
            video.setMediaController(ctlr);
            video.requestFocus();
            video.start();
        }
    }
}