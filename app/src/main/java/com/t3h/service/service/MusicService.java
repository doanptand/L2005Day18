package com.t3h.service.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.t3h.service.R;
import com.t3h.service.media.MediaManager;
import com.t3h.service.util.Const;

import java.util.logging.Logger;

public class MusicService extends Service {
    private MediaManager mediaManager;
    private MusicBinder binder = new MusicBinder();
    private MusicReceiver musicReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("doanpt", "onCreate");
        mediaManager = new MediaManager(this);
        musicReceiver = new MusicReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.ACTION_NEXT);
        filter.addAction(Const.ACTION_PAUSE);
        filter.addAction(Const.ACTION_PREVIOUS);
        filter.addAction(Const.ACTION_PLAY);
        registerReceiver(musicReceiver, filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaManager.play();
        pushNotification();
        Log.d("doanpt", "onStartCommand");
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pushNotification() {
        Context context;
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Music");
        builder.setContentText("Song name");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationChannel channel = new NotificationChannel(
                "123", "123",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        builder.setChannelId(channel.getId());

        Notification notification = builder.build();

//        manager.notify(1, notification);
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        Log.d("doanpt", "onDestroy");
        unregisterReceiver(musicReceiver);
        super.onDestroy();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    public MediaManager getMediaManager() {
        return mediaManager;
    }

    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("doanpt", "onReceive action:" + intent.getAction());
            switch (intent.getAction()) {
                case Const.ACTION_PLAY:
                    mediaManager.play();
                    break;
                case Const.ACTION_PREVIOUS:
                    mediaManager.previous();
                    break;
                case Const.ACTION_PAUSE:
                    mediaManager.pause();
                    break;
                case Const.ACTION_NEXT:
                    mediaManager.next();
                    break;

            }
        }
    }
}
