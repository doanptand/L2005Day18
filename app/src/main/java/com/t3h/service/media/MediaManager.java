package com.t3h.service.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;


import com.t3h.service.model.SongItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaManager {
    private static final int STATE_STOP = -1;
    private static final int STATE_PLAYING = 0;
    private static final int STATE_PAUSE = 1;
    private int playState = STATE_STOP;

    private static final String TAG = "MediaManager";
    private MediaPlayer mPlayer;
    private List<SongItem> arrSongs = new ArrayList<>();
    private Context context;
    private int currentIndex = 0;

    public MediaManager(Context context) {
        this.context = context;
        getAllAudioFilesExternal();
        initPlayer();
    }

    public void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                Log.i(TAG, "onPrepared...");
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "onError...");
                playState = STATE_STOP;
                return false;
            }
        });
    }

    public void play() {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(context, Uri.parse(arrSongs.get(currentIndex).getPath()));
            mPlayer.prepare();
            playState = STATE_PLAYING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void next() {
        if (currentIndex >= arrSongs.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        play();
    }

    public void previous() {
        if (currentIndex <= 0) {
            currentIndex = arrSongs.size() - 1;
        } else {
            currentIndex--;
        }
        play();
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            playState = STATE_PAUSE;
        } else {
            mPlayer.start();
            playState = STATE_PLAYING;
        }
    }

    public void stop() {
        //mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }

    public void seek(int seekTo) {
        mPlayer.seekTo(seekTo);
//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getAllAudioFilesExternal() {
        String columnsName[] = new String[]{MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE};
        Cursor c = context.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columnsName, null, null, null, null);
        c.moveToFirst();

        arrSongs.clear();
        int pathIndex = c.getColumnIndex(columnsName[0]);
        int fullName = c.getColumnIndex(columnsName[1]);
        int durationIndex = c.getColumnIndex(columnsName[2]);
        int authorIndex = c.getColumnIndex(columnsName[3]);
        int songNameIndex = c.getColumnIndex(columnsName[4]);

        while (c.isAfterLast() == false) {
            SongItem item = new SongItem(c.getString(songNameIndex),
                    c.getString(pathIndex),
                    c.getString(durationIndex),
                    c.getString(authorIndex),
                    c.getString(fullName));
            arrSongs.add(item);
            c.moveToNext();
        }
        c.close();
    }
}
