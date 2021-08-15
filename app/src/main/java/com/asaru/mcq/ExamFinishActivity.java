package com.asaru.mcq;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ExamFinishActivity extends AppCompatActivity {
    TextView correct,wrong;
    ArrayList<wrongAnswer> wrongQuestions = new ArrayList<>();
    ArrayList<correctAnswer> correctQuestions = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_finish);
        correct=findViewById(R.id.correct);
        wrong=findViewById(R.id.wrong);
        Intent i=getIntent();
       //int z=i.getExtras().getInt("time");
        wrongQuestions=i.getExtras().getParcelableArrayList("wrongAnswerArray");
        correctQuestions=i.getExtras().getParcelableArrayList("correctAnswerArray");

            correct.setText(Integer.toString( correctQuestions.size()));
        if (wrongQuestions.isEmpty()){
            wrong.setText("is empty");
        }else{

            wrong.setText(Integer.toString(wrongQuestions.size()));
        }



    }
}