package com.momo.dev.momo;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private String FileName = null;
    private Byte RecorderStatus = 0;

    private Button startrecord;
    private Button stoprecord;
    private Button startplayback;
    private Button stopplayback;

    private Chronometer chronometer;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startrecord = (Button)findViewById(R.id.bt_start_record);
        startrecord.setOnClickListener(new startRecordListener());
        stoprecord = (Button)findViewById(R.id.bt_stop_record);
        stoprecord.setOnClickListener(new stopRecordListener());
        startplayback = (Button)findViewById(R.id.bt_start_playback);
        startplayback.setOnClickListener(new startPlayListener());
        stopplayback = (Button)findViewById(R.id.bt_stop_playback);
        stopplayback.setOnClickListener(new stopPlayListener());
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/momo/";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                startRecord();
                Toast.makeText(this,"开始录音",Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                stopRecord();
                Toast.makeText(this,"结束录音",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //return super.onKeyDown(keyCode, event);
        return true;
    }

    void startRecord(){
        if(RecorderStatus==1){
            return;
        }
        RecorderStatus = 1;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setOutputFile(FileName + sDateFormat.format(new java.util.Date()) + ".aac");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
    void stopRecord(){
        if(RecorderStatus==0){
            return;
        }
        RecorderStatus = 0;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;                       //会被回收
        chronometer.stop();
    }
    class startRecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            startRecord();
        }

    }
    //停止录音
    class stopRecordListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            stopRecord();
        }

    }
    //播放录音
    class startPlayListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPlayer = new MediaPlayer();
            try{
                mPlayer.setDataSource(FileName);
                mPlayer.prepare();
                mPlayer.start();
            }catch(IOException e){
                Log.e(LOG_TAG,"播放失败");
            }
        }

    }
    //停止播放录音
    class stopPlayListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPlayer.release();
            mPlayer = null;
        }

    }
}
