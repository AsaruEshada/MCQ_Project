package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExamFinishActivity extends AppCompatActivity {
TextView time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_finish);
        time=findViewById(R.id.time);
        Intent i=getIntent();
        int z=i.getExtras().getInt("time");
        time.setText(String.valueOf(z));
    }
}