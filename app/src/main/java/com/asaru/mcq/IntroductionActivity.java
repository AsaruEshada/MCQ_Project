package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroductionActivity extends AppCompatActivity {
Button next,back;
String year,subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        next=findViewById(R.id.next);
        back=findViewById(R.id.back);

        year=getIntent().getStringExtra("year");
        subject=getIntent().getStringExtra("subject");
        back.setOnClickListener(v -> onBackPressed());
        next.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ExamActivity.class);
            i.putExtra("subject", subject);
            i.putExtra("year", year);
        });
    }
}