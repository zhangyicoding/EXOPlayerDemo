package com.estyle.exoplayerdemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

//    private final String VIDEO_URL = "http://cdn-grindr.static.kunlun.com/video/mp4/1378032ebf1e3f4738c698de816a51e5";
    private final String VIDEO_URL = "https://wdl.wallstreetcn.com/41aae4d2-390a-48ff-9230-ee865552e72d";

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaSource mediaSource;

    private DefaultTrackSelector trackSelector;
    private DataSource.Factory mediaDataSourceFactory;

    private HttpProxyCacheServer mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

        mServer = new HttpProxyCacheServer(this);

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);

//        mediaSource = buildMediaSource(Uri.fromFile(new File(getCachePath(VIDEO_URL))), null);
        mediaSource = buildMediaSource(Uri.parse(VIDEO_URL), null);
//        simpleExoPlayer.prepare(mediaSource);
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleExoPlayer.prepare(mediaSource);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
            : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                    new Handler(), new ExtractorMediaSource.EventListener() {
                    @Override
                    public void onLoadError(IOException error) {
                    }
                });
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
            buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "ExoPlayerDemo"), bandwidthMeter);
    }

    public String getCachePath(String url) {
        return mServer.getProxyUrl(url);
    }

}
