package com.estyle.exoplayerdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class MyExoPlayerFactory {

    public static SimpleExoPlayer newExoPlayerInstance(Context context, SimpleExoPlayerView playerView) {
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                new DefaultBandwidthMeter());
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        playerView.setPlayer(simpleExoPlayer);

        simpleExoPlayer.setPlayWhenReady(true);
        return simpleExoPlayer;
    }

    public static MediaSource newMediaSourceInstance(Context context, String url) {
        HttpProxyCacheServer httpProxyCacheServer = new HttpProxyCacheServer(context);
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,
                "EXOPlayerDemo"));
        return new ExtractorMediaSource(Uri.parse(httpProxyCacheServer.getProxyUrl(url)), mediaDataSourceFactory,
                new DefaultExtractorsFactory(),
                new Handler(), new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {
            }
        });
    }

}
