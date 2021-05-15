package com.asaru.mcq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroductionActivity extends AppCompatActivity {
Button next,back;
String selectedYear, selectedSubject,lang;
TextView subject,chosenSubject,year,chosenYear,des;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        next=findViewById(R.id.next);
        back=findViewById(R.id.back);
        subject=findViewById(R.id.subject);
        chosenSubject=findViewById(R.id.chosen_subject);
        year=findViewById(R.id.year);
        chosenYear=findViewById(R.id.chosen_year);
        des=findViewById(R.id.des);
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        lang = language.getString("Language", "");
        context = ChangeLanguage.setLocale(IntroductionActivity.this, lang);
        resources = context.getResources();

        selectedYear =getIntent().getStringExtra("year");
        selectedSubject =getIntent().getStringExtra("subject");
        setLanguage();
        back.setOnClickListener(v -> onBackPressed());
        next.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ExamActivity.class);
            i.putExtra("subject", selectedSubject);
            i.putExtra("year", selectedYear);
            startActivity(i);
        });
    }
    public void setLanguage(){
        subject.setText(resources.getText(R.string.chosen_subject));
        year.setText(resources.getText(R.string.chosen_year));
        des.setText(resources.getText(R.string.exam_des));
        chosenSubject.setText(selectedSubject);
        chosenYear.setText(selectedYear);
        next.setText(resources.getText(R.string.next_step));
        back.setText(resources.getText(R.string.back_step));

    }
}