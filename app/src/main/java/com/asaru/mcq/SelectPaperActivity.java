package com.asaru.mcq;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectPaperActivity extends AppCompatActivity {
TextView description,path,subject,year;
Context context;
Resources resources;
Spinner pathSpinner,subjectSpinner,yearSpinner;
ImageView back;
private ArrayAdapter<CharSequence> arrayAdapter,yearAdapter;
    String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_paper);
        //check language
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        lang=language.getString("Language","");
        context=ChangeLanguage.setLocale(SelectPaperActivity.this,lang);
        resources=context.getResources();

        description=findViewById(R.id.description);
        path=findViewById(R.id.path);
        subject=findViewById(R.id.subject);
        year=findViewById(R.id.year);
        pathSpinner=findViewById(R.id.Path);
        subjectSpinner=findViewById(R.id.Subject);
        yearSpinner=findViewById(R.id.Year);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setLanguage();
        arrayAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_item, resources.getStringArray(R.array.Path_array));
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        pathSpinner.setAdapter(arrayAdapter);
        yearAdapter= new ArrayAdapter<CharSequence>(this, R.layout.spinner_item, resources.getStringArray(R.array.year));
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


    }
    public void setLanguage(){

        description.setText(resources.getText(R.string.ExamType));
        path.setText(resources.getText(R.string.Path));
        subject.setText(resources.getText(R.string.Subject));
        year.setText(resources.getText(R.string.Year));
    }
}