package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExamFinishActivity extends AppCompatActivity {
    TextView correct,wrong;
    ArrayList<wrongAnswer> wrongQuestions = new ArrayList<>();
    ArrayList<correctAnswer> correctQuestions = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_finish);
        correct=findViewById(R.id.correct);
        wrong=findViewById(R.id.wrong);
        Intent i=getIntent();
       //int z=i.getExtras().getInt("time");
        //wrongQuestions=i.getExtras().getParcelableArrayList("wrongAnswerArray");
        //correctQuestions=i.getExtras().getParcelableArrayList("correctAnswerArray");
      sharedPreferences = getApplicationContext().getSharedPreferences("shared Preferences", MODE_PRIVATE);


        Gson gson = new Gson();
        String correct = sharedPreferences.getString("correctAnswerArray", "");
        String wrong = sharedPreferences.getString("wrongAnswerArray", "");
        Type type1= new TypeToken<List<wrongAnswer>>(){}.getType();
        Type type= new TypeToken<List<correctAnswer>>(){}.getType();
        List<wrongAnswer> wrongQuestion=gson.fromJson(wrong,type1);
        List<correctAnswer> correctQuestion=gson.fromJson(correct,type);
        wrongQuestions= (ArrayList<wrongAnswer>) wrongQuestion;
        correctQuestions= (ArrayList<correctAnswer>) correctQuestion;
        Log.d("correct", String.valueOf(correctQuestions.size()));
        Log.d("wrong", String.valueOf(wrongQuestions.size()));




    }
}