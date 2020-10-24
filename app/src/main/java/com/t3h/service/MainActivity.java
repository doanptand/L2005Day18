package com.t3h.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.t3h.service.receiver.FirstReceiver;
import com.t3h.service.service.MusicService;
import com.t3h.service.util.Const;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int STORAGE_PERMISSION = 1000;
    private SeekBar sbTime;
    private Button btnPrevious, btnPlay, btnNext, btnPause;
    private MusicService musicService;

    private FirstReceiver firstReceiver;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            musicService = binder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION);
        } else {
            startMusic();
        }
        register();
    }

    private void register() {
        firstReceiver = new FirstReceiver();

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter1.addAction(Intent.ACTION_SCREEN_ON);
        filter1.addAction("com.doan.deptrai");

        registerReceiver(firstReceiver, filter1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(firstReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMusic();
            } else {
                Toast.makeText(this, "you are so stupid", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void startMusic() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void initViews() {
        sbTime = findViewById(R.id.sb_time);
        btnNext = findViewById(R.id.btn_next);
        btnPlay = findViewById(R.id.btn_play);
        btnPrevious = findViewById(R.id.btn_previous);
        btnPause = findViewById(R.id.btn_pause);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);


        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                nextSong();
//                sendDemoAction();
//                if (musicService != null) {
//                    musicService.getMediaManager().next();
//                }
                break;
            case R.id.btn_previous:
                previousSong();
//                if (musicService != null) {
//                    musicService.getMediaManager().previous();
//                }
                break;
            case R.id.btn_play:
                playSong();
//                if (musicService != null) {
//                    musicService.getMediaManager().play();
//                }
                break;
            case R.id.btn_pause:
                pauseSong();
//                if (musicService != null) {
//                    musicService.getMediaManager().pause();
//                }
                break;
        }
    }

    private void nextSong() {
        Intent intent = new Intent(Const.ACTION_NEXT);
        sendBroadcast(intent);
    }
    private void previousSong() {
        Intent intent = new Intent(Const.ACTION_PREVIOUS);
        sendBroadcast(intent);
    }
    private void playSong() {
        Intent intent = new Intent(Const.ACTION_PLAY);
        sendBroadcast(intent);
    }
    private void pauseSong() {
        Intent intent = new Intent(Const.ACTION_PAUSE);
        sendBroadcast(intent);
    }

    private void sendDemoAction() {
        Intent intent = new Intent("com.doan.deptrai");
        sendBroadcast(intent);
    }
}