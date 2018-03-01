package net.pupil.newlife;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.pupil.newlife.bluetooth.BluetoothActivity;
import net.pupil.newlife.crimialintent.CrimeListActivity;
import net.pupil.newlife.expandablelistview.ExpandableActivity;
import net.pupil.newlife.im.ImActivity;

import java.io.IOException;

/**
 * 仔细维护好这个应用 2017.3.13 15:22
 * <p>
 * 应用的主界面，集成所有编写的小Demo
 */
public class MainActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private int YOUR_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void crime(View view) {
        startActivity(new Intent(this, CrimeListActivity.class));
    }

    public void im(View view) {
        startActivity(new Intent(this, ImActivity.class));
    }

    public void expandable(View view) {
        startActivity(new Intent(this, ExpandableActivity.class));
    }

    public void startRecord(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, YOUR_REQUEST_CODE);
            } else {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/xxt_he/audio.m4a");
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecorder.start();
            }
        } else {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/xxt_he/audio.m4a");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }

    }

    public void stopRecord(View view) {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(requestCode == YOUR_REQUEST_CODE)
            {
                Log.i("test", "Permission granted");
                //do what you wanted to do
            }
        } else {
            Log.d("test", "Permission failed");
        }
    }

    public void bluetooth(View view) {
        startActivity(new Intent(this, BluetoothActivity.class));
    }

}
