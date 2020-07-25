package net.pupil.newlife.career;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.pupil.newlife.R;
import net.pupil.newlife.common.PupilOkHttp3Utils;
import net.pupil.newlife.common.PupilToastUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PupilLogicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_logic);

        findViewById(R.id.cannot_click_quickly_btn).setOnClickListener(new PupilOnClickListener(v -> {
            okhttp3Call();
        }));
    }

    private void okhttp3Call() {
        PupilOkHttp3Utils.callGet("https://www.baidu.com", () -> {
            PupilToastUtils.toastShort(this, "zzz");
        });
    }

    //TODO
    // 4.Github
}