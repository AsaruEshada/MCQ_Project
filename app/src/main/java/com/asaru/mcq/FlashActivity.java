package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class FlashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        Context context=this;
        int splash = 4500;
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        String lang=language.getString("Language","");
        new Handler().postDelayed(() -> {
            if (lang.isEmpty()){
            Intent i =new Intent(context,SelectLanguageActivity.class);
            startActivity(i);
            finish();}
            else {
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        }
                , splash);

    }

}