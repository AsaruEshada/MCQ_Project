package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;

public class SelectLanguageActivity extends AppCompatActivity {
public RadioGroup radioGroup;
    private Button go;
    Context context;
    Resources resources;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        radioGroup=findViewById(R.id.group);
        go=findViewById(R.id.go);
        go.setOnClickListener(v -> {
            if (radioGroup.getCheckedRadioButtonId()== -1){
                 Toast.makeText(this,"Please Select the Language",Toast.LENGTH_LONG).show();
            }else{
                SharedPreferences settings = getApplicationContext().getSharedPreferences("language", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Language", lang);
                Log.d("Language",lang);
                editor.apply();
                    startActivity(new Intent(context, MainActivity.class));
            }
        });

    }
    public void  checkButton(View v){
        int radioId=radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        if (radioButton.getText().equals("English")){
            lang="en";
            context=ChangeLanguage.setLocale(SelectLanguageActivity.this,"en");


        }else{
            if(radioButton.getText().equals("සිංහල")){
                lang="si";
                context=ChangeLanguage.setLocale(SelectLanguageActivity.this,"si");

            }else{
                lang="ta";
                context=ChangeLanguage.setLocale(SelectLanguageActivity.this,"ta");

            }
        }
        resources=context.getResources();
        go.setText(resources.getText(R.string.language_selected_button));
    }
}