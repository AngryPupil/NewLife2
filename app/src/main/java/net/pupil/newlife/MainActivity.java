package net.pupil.newlife;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;

import net.pupil.newlife.bluetooth.BluetoothActivity;
import net.pupil.newlife.career.ExpandableRecyclerActivity;
import net.pupil.newlife.career.PupilLogicActivity;
import net.pupil.newlife.crimialintent.CrimeListActivity;
import net.pupil.newlife.devart.CustomViewActivity;
import net.pupil.newlife.devart.DevArtActivity;
import net.pupil.newlife.devart.SlideConflictActivity;
import net.pupil.newlife.devart.SlideConflictPortraitActivity;
import net.pupil.newlife.expandablelistview.ExpandableActivity;
import net.pupil.newlife.im.ImActivity;
import net.pupil.newlife.ndk.AssetExtractor;
import net.pupil.newlife.ndk.Util;
import net.pupil.newlife.vim.VIMActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 仔细维护好这个应用 2017.3.13 15:22
 * <p>
 * 应用的主界面，集成所有编写的小Demo
 */
public class MainActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private int YOUR_REQUEST_CODE = 200;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initPIL();

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("MainActivity", "permission:" + permission);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        } else {
//            try {
//                FileReader fr = new FileReader("/sdcard/download/test.txt");
//                BufferedReader r = new BufferedReader(fr);
//                String result = r.readLine();
//                Log.d("MainActivity", "SD卡文件里面的内容:" + result);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        final Button btn = (Button) findViewById(R.id.pupil_logic_btn);
        btn.post(new Runnable() {
            @Override
            public void run() {
                btn.performClick();
            }
        });
    }

    private void initPIL() {
        File dir = new File(getFilesDir(), "python");
        dir.mkdirs();
        final File file = new File(getFilesDir(), "python/PIL.zip");//getFilesDir()方法用于获取/data/data//files目录
        System.out.println("文件路径---->"+getFilesDir());
        if(file.exists()){//文件存在了就不需要拷贝了
            System.out.println("文件已经存在,不需要再拷贝");
            return;
        }
        new Thread(){
            public void run() {
                System.out.println("进行文件拷贝");
                try {
                    //获取资产目录管理器
                    AssetManager assetManager = getAssets();
                    InputStream is = assetManager.open("python/PIL.zip");//输入流
                    FileOutputStream fos = new FileOutputStream(file);//输出流
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len=is.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    public void testNdk(View view) {
        AssetExtractor assetExtractor = new AssetExtractor(this);
        assetExtractor.removeAssets("python");
        assetExtractor.copyAssets("python");

        final String pythonPath = assetExtractor.getAssetsDataDir() + "python";

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                Log.d("---> ",""+ Util.run(pythonPath));
            }
        });
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

    public void openVIM(View view) {
        startActivity(new Intent(this, VIMActivity.class));
    }

    public void devArt(View view) {
        startActivity(new Intent(this, DevArtActivity.class));
//        Intent intent = new Intent();
//        intent.setClass(this, DevArtActivity.class);
//        getApplicationContext().startActivity(intent);
    }

    public void slideConflict(View view) {
        startActivity(new Intent(this, SlideConflictActivity.class));
    }

    public void slideConflictPortrait(View view) {
        startActivity(new Intent(this, SlideConflictPortraitActivity.class));
    }

    public void custom_view(View view) {
        startActivity(new Intent(this, CustomViewActivity.class));
    }

    public void expandable_recycler(View view) {
        startActivity(new Intent(this, ExpandableRecyclerActivity.class));
    }

    public void pupilLogic(View view) {
        startActivity(new Intent(this, PupilLogicActivity.class));
    }
}
