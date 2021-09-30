package com.asaru.mcq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DoLaterQuestionActivity extends AppCompatActivity {
    TextView subject, year, exit, questionView, time, questionNumber, doneQuestions;
    Button next, back, doLater, finish;
    RadioGroup radioGroup;
    String selectedYear;
    String selectedSubject;
    String correctAnswer;
    String questionUrl;
    Integer answerTotalQuestion, correctAnswerNo, wrongAnswerNo, questionNum;
    private long durationMillisecond;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    SharedPreferences sharedPreferences;
    ArrayList<doLater> doLaterArrayList = new ArrayList<>();
    Integer questionNo;
    ArrayList<wrongAnswer> wrongQuestions = new ArrayList<>();
    ArrayList<correctAnswer> correctQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_later_question);
        subject = findViewById(R.id.subject);
        year = findViewById(R.id.year);
        exit = findViewById(R.id.exit);
        questionView = findViewById(R.id.question);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        doLater = findViewById(R.id.later);
        finish = findViewById(R.id.finish);
        radioGroup = findViewById(R.id.radioGroup);
        time = findViewById(R.id.time);
        questionNumber = findViewById(R.id.questionNo);
        doneQuestions = findViewById(R.id.doneQuestion);
        collectIntentValue();
        startTimer();
        sharedPreferences = getApplicationContext().getSharedPreferences("shared Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // get do later questions from shared
        Gson gson = new Gson();
        String doLater = sharedPreferences.getString("doLaterArray", "");
        Type type = new TypeToken<List<doLater>>() {
        }.getType();
        List<doLater> doLaterList = gson.fromJson(doLater, type);
        doLaterArrayList = (ArrayList<doLater>) doLaterList;

        update(questionNum);
        next.setOnClickListener(v -> {
            if (timerRunning) {
                if (questionNum < 0) {
                    countDownTimer.cancel();
                    paperOver(editor);

                } else {
                    checkAnswer(editor);
                }

            } else {
                Toast.makeText(getApplicationContext(), "Your time is over", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void update(int questionNum) {

        questionUrl = doLaterArrayList.get(questionNum).questionImage;
        correctAnswer = doLaterArrayList.get(questionNum).answer;
        questionNo = doLaterArrayList.get(questionNum).question;
        questionView.setText(questionUrl);
    }

    private void collectIntentValue() {
        selectedYear = getIntent().getStringExtra("year");
        selectedSubject = getIntent().getStringExtra("subject");
        durationMillisecond = getIntent().getLongExtra("time", 0);
        correctAnswerNo = getIntent().getIntExtra("correct answers", 0);
        wrongAnswerNo = getIntent().getIntExtra("wrong Answers", 0);
        questionNum = getIntent().getIntExtra("do later total", 0) - 1;
        subject.setText(selectedSubject);
        year.setText(selectedYear);

        sharedPreferences = getApplicationContext().getSharedPreferences("shared Preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String correct = sharedPreferences.getString("correctAnswerArray", "");
        String wrong = sharedPreferences.getString("wrongAnswerArray", "");
        Type type1 = new TypeToken<List<wrongAnswer>>() {
        }.getType();
        Type type = new TypeToken<List<correctAnswer>>() {
        }.getType();
        List<wrongAnswer> wrongQuestion = gson.fromJson(wrong, type1);
        List<correctAnswer> correctQuestion = gson.fromJson(correct, type);
        wrongQuestions = (ArrayList<wrongAnswer>) wrongQuestion;
        correctQuestions = (ArrayList<correctAnswer>) correctQuestion;
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(durationMillisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                durationMillisecond = millisUntilFinished;

                int min = (int) durationMillisecond / 60000;


                int second = (int) durationMillisecond % 60000 / 1000;
                String sTime;
                sTime = "" + min;
                sTime += ":";
                if (second < 10) sTime += "0";
                sTime += second;
                time.setText(sTime);
                timerRunning = true;


            }

            @Override
            public void onFinish() {
                timerRunning = false;
            }
        }.start();
    }

    public void checkAnswer(SharedPreferences.Editor editor) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_LONG).show();
        } else {
            int radioId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioId);
            CharSequence userAnswer;
            userAnswer = radioButton.getText();

            if (userAnswer.equals(correctAnswer)) {
                com.asaru.mcq.correctAnswer correctAnswerArray = new correctAnswer(questionNo, correctAnswer, questionUrl);

                correctQuestions.add(correctAnswerNo, correctAnswerArray);

                correctAnswerNo++;

            } else {
                wrongAnswer wrongAnswerArray = new wrongAnswer(questionNo, (String) userAnswer, correctAnswer, questionUrl);


                wrongQuestions.add(wrongAnswerNo, wrongAnswerArray);

                wrongAnswerNo++;

            }
            questionNum--;

            radioGroup.clearCheck();
            if (questionNum < 0) {
                paperOver(editor);
            } else {
                update(questionNum);
            }


        }
    }

    public void paperOver(SharedPreferences.Editor editor) {
        Intent i = new Intent(getApplicationContext(), ExamFinishActivity.class);
        Gson gson1 = new Gson();
        String correct = gson1.toJson(correctQuestions);
        String wrong = gson1.toJson(wrongQuestions);
        editor.putString("correctAnswerArray", correct);
        editor.putString("wrongAnswerArray", wrong);
        Log.d("Language1", correct);
        Log.d("Language2", wrong);
        editor.apply();
        startActivity(i);
        finish();
    }
}