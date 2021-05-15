package com.asaru.mcq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectPaperActivity extends AppCompatActivity {
    TextView description, path, subject, year, git, common;
    Context context;
    Resources resources;
    Spinner pathSpinner, subjectSpinner, yearSpinner;
    ImageView back;
    String lang;
    ArrayAdapter<String> subjectAdapter;
    Button apply;
    String selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_paper);
        //check language
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        lang = language.getString("Language", "");
        context = ChangeLanguage.setLocale(SelectPaperActivity.this, lang);
        resources = context.getResources();

        description = findViewById(R.id.description);
        path = findViewById(R.id.path);
        subject = findViewById(R.id.subject);
        year = findViewById(R.id.year);
        pathSpinner = findViewById(R.id.Path);
        subjectSpinner = findViewById(R.id.Subject);
        yearSpinner = findViewById(R.id.Year);
        back = findViewById(R.id.back);
        git = findViewById(R.id.git);
        common = findViewById(R.id.common);
        apply = findViewById(R.id.apply);
        back.setOnClickListener(v -> onBackPressed());


        setLanguage();
        ArrayAdapter<CharSequence> pathAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, resources.getStringArray(R.array.Path_array));
        pathAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        pathSpinner.setAdapter(pathAdapter);

        subjectAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, resources.getStringArray(R.array.Math_subject));

        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, resources.getStringArray(R.array.year));
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        pathSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subjectAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.Math_subject));
                }
                if (position == 1) {
                    subjectAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.Language_subject));
                }
                if (position == 2) {
                    subjectAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.Art_subject));
                }
                if (position == 3) {
                    subjectAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.Commerce_subject));
                }
                subjectAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                subjectSpinner.setAdapter(subjectAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        apply.setOnClickListener(v -> {
            selectedSubject = subjectSpinner.getSelectedItem().toString();
            Intent i = new Intent(getApplicationContext(), IntroductionActivity.class);
            i.putExtra("subject", selectedSubject);
            i.putExtra("year", yearSpinner.getSelectedItem().toString());
            startActivity(i);
        });
    }

    public void setLanguage() {
        if (lang.equals("en")) {
            path.setVisibility(View.GONE);
            pathSpinner.setVisibility(View.GONE);
            subjectAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, resources.getStringArray(R.array.english_medium));
            subjectAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            subjectSpinner.setAdapter(subjectAdapter);
        }
        git.setText(resources.getText(R.string.GIT));
        common.setText(resources.getText(R.string.common_test));
        description.setText(resources.getText(R.string.ExamType));
        path.setText(resources.getText(R.string.Path));
        subject.setText(resources.getText(R.string.Subject));
        year.setText(resources.getText(R.string.Year));

    }
}