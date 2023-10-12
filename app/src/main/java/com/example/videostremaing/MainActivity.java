package com.example.videostremaing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class MainActivity extends AppCompatActivity {
    // Md.Sakib Ur Rahman(37)
    //       Ferdous Mondol(52)
    //      Sourove Hossen(90)
    ExoPlayer player;
    StyledPlayerView playerView;
    String videourl = "https://video.blender.org/download/videos/3d95fb3d-c866-42c8-9db1-fe82f48ccb95-804.mp4";
    private int currentOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    private boolean isVideoPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.exoplayer_view);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videourl);
        player.setMediaItem(mediaItem);
        player.prepare();

        // Set the player's play state change listener
        player.addListener(new ExoPlayer.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                isVideoPlaying = isPlaying;
                updateSystemUiVisibility();
            }
        });

        // Create an OrientationEventListener to handle orientation changes
        OrientationEventListener orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                setOrientation(orientation);
            }
        };

        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    }

    private void setOrientation(int orientation) {
        // Determine the requested screen orientation based on the device orientation
        int requestedOrientation;
        if (orientation >= 45 && orientation < 135) {
            // Landscape
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        } else if (orientation >= 135 && orientation < 225) {
            // Portrait (reverse)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
           // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        } else if (orientation >= 225 && orientation < 315) {
            // Landscape (reverse)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        } else {
            // Portrait
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        }

        // Set the requested orientation if it's different from the current orientation
        if (requestedOrientation != currentOrientation) {
            setRequestedOrientation(requestedOrientation);
            currentOrientation = requestedOrientation;
        }
    }

    private void updateSystemUiVisibility() {
        // Hide the system UI (status bar) when the video is playing
        if (isVideoPlaying) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            // Clear the fullscreen flag when the video stops
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        player.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
    }
}
