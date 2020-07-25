package net.pupil.newlife.devart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.pupil.newlife.R;

import java.util.ArrayList;
import java.util.List;

public class SlideConflictPortraitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_conflict_portrait);

        ListView list = (ListView) findViewById(R.id.list_view);

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add("list" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, datas);
        list.setAdapter(adapter);
    }
}
