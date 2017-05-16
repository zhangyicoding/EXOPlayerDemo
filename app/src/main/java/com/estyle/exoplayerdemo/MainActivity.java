package com.estyle.exoplayerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class MainActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private static final String VIDEO_URL = "http://html5demos.com/assets/dizzy.mp4";

    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        player = MyExoPlayerFactory.newExoPlayerInstance(this, simpleExoPlayerView);
        player.addListener(this);
        player.prepare(MyExoPlayerFactory.newMediaSourceInstance(this, VIDEO_URL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        player.removeListener(this);
        player = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        EstyleLog.e("playWhenReady : " + playWhenReady);
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                EstyleLog.e("buffering");
                break;
            case ExoPlayer.STATE_IDLE:
                EstyleLog.e("idle(fail play)");
                break;
            case ExoPlayer.STATE_READY:
                EstyleLog.e("ready(start playing)");
                break;
            case ExoPlayer.STATE_ENDED:
                EstyleLog.e("end(play completed)");
                break;
        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
