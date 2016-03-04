package com.example.myname.servicesdemo;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MusicPlayer extends Service implements MediaPlayer.OnCompletionListener {
    public static final String ACTION_ADD_TO_QUEUE = "com.services.ADD_TO_QUEUE";
    private ConcurrentLinkedQueue<Uri> mTrackQueue;
    private MediaPlayer mMediaPlayer;

    private MusicPlayerBinder mMusicPlayerBinder = new MusicPlayerBinder();
    private Callback mCallback;

    public IBinder onBind(Intent intent) {
        return mMusicPlayerBinder;
    }

    public class MusicPlayerBinder extends Binder {
        public MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTrackQueue = new ConcurrentLinkedQueue<Uri>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_ADD_TO_QUEUE.equals(action)) {
            Uri trackUri = intent.getData();
            addTrackToQueue(trackUri);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Add track to end of queue if already playing,
     * otherwise create a new MediaPlayer and start playing.
     */
    private void addTrackToQueue(Uri trackUri) {
        if(mMediaPlayer == null) {
            try {
                mMediaPlayer = MediaPlayer.create(this, trackUri);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                stopSelf();
            }
        } else {
            mTrackQueue.offer(trackUri);
        }
    }

    // Track completed, start playing next or stop service...
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        Uri nextTrackUri = mTrackQueue.poll();
        if(nextTrackUri != null) {
            try {
                mMediaPlayer.setDataSource(this, nextTrackUri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                stopSelf();
            }
        } else {
            stopSelf();
        }
    }

    public interface Callback {
        void onOperationProgress(int progress);
        void onOperationCompleted(ServResult complexResult);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }
}
