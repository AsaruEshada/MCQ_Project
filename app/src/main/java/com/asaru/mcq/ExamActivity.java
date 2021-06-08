package com.asaru.mcq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ExamActivity extends AppCompatActivity {
    String selectedYear, selectedSubject, lang, medium, correctAnswer;
    TextView subject, year, exit, question, time, questionNumber;
    FirebaseDatabase database;
    DatabaseReference myRef, ref;
    Button next, back, doLater, finish;
    Integer questionNo = 1;
    Integer totalQuestionNo = 0, doLaterTotal = 0, answerTotalQuestion = 0, correctAnswerNo = 0, wrongAnswerNo = 0;
    CharSequence userAnswer;
    RadioGroup radioGroup;
    Integer totalQuestions,moreTime;
    private CountDownTimer countDownTimer;
    private long durationMillisecond = 600000 * 12;
    private boolean timerRunning;
    ArrayList<Integer> doLaterQuestions = new ArrayList<>();
    ArrayList<Integer> correctQuestionNo = new ArrayList<>();
    ArrayList<Integer> wrongQuestionNo = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        subject = findViewById(R.id.subject);
        year = findViewById(R.id.year);
        exit = findViewById(R.id.exit);
        question = findViewById(R.id.question);
        selectedYear = getIntent().getStringExtra("year");
        selectedSubject = getIntent().getStringExtra("subject");
        subject.setText(selectedSubject);
        year.setText(selectedYear);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        doLater = findViewById(R.id.later);
        finish = findViewById(R.id.finish);
        radioGroup = findViewById(R.id.radioGroup);
        time = findViewById(R.id.time);
        questionNumber = findViewById(R.id.questionNo);

        exit.setOnClickListener(v ->
        {
            startActivity(new Intent(getApplicationContext(), SelectPaperActivity.class));
            finish();
        });
        database = FirebaseDatabase.getInstance();
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        lang = language.getString("Language", "");

        updateQuestion();
        checkQuestions();
        //countdown timer
        startTimer();


        next.setOnClickListener(v -> {

            if (timerRunning) {
                if (questionNo.equals(totalQuestions)) {
                    if (doLaterTotal.equals(0)) {

                        countDownTimer.cancel();

                        Intent i=new Intent(getApplicationContext(),ExamFinishActivity.class);
                        i.putExtra("time",moreTime);
                        i.putExtra("wrong",wrongAnswerNo);
                        i.putExtra("correct",correctAnswerNo);
                        i.putExtra("wrongAnswer",wrongQuestionNo);
                        i.putExtra("correctAnswer",correctQuestionNo);

                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "do later", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_LONG).show();
                    } else {
                        int radioId = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = findViewById(radioId);
                        userAnswer = radioButton.getText();
                        Toast.makeText(getApplicationContext(), userAnswer, Toast.LENGTH_SHORT).show();
                        if (userAnswer.equals(correctAnswer)) {
                            correctQuestionNo.add(correctAnswerNo, questionNo);
                            correctAnswerNo++;
                        } else {
                            wrongQuestionNo.add(wrongAnswerNo, questionNo);
                            wrongAnswerNo++;
                        }
                        questionNo++;
                        answerTotalQuestion++;
                        radioGroup.clearCheck();
                        updateQuestion();

                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Your time is over", Toast.LENGTH_LONG).show();
            }
        });
        doLater.setOnClickListener(v -> {
            doLaterQuestions.add(doLaterTotal, questionNo);
            questionNo++;
            doLaterTotal++;
            updateQuestion();
        });
        back.setOnClickListener(v -> {
            if (correctAnswerNo > 0) {
                if (correctQuestionNo.get(correctAnswerNo - 1).equals(questionNo - 1)) {
                    correctQuestionNo.remove(correctAnswerNo--);
                    correctAnswerNo = correctAnswerNo - 1;
                }
            }
            if (wrongAnswerNo > 0) {
                if (wrongQuestionNo.get(wrongAnswerNo - 1).equals(questionNo - 1)) {
                    Log.d("answer", wrongQuestionNo.get(wrongAnswerNo - 1).toString());
                    wrongQuestionNo.remove(wrongAnswerNo - 1);
                    wrongAnswerNo = wrongAnswerNo - 1;
                }
            }
            if (doLaterTotal > 0) {
                if (doLaterQuestions.get(doLaterTotal - 1).equals(questionNo - 1)) {
                    doLaterQuestions.remove(doLaterTotal - 1);
                    doLaterTotal = doLaterTotal - 1;
                }
            }


            questionNo = questionNo - 1;
            updateQuestion();
        });
        finish.setOnClickListener(v -> {
            if (questionNo == totalQuestions){

            }if (questionNo<2){
                startActivity(new Intent(getApplicationContext(), SelectPaperActivity.class));
                finish();
            }if (questionNo>2){

            }
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void updateQuestion() {
        if (questionNo > 1) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.INVISIBLE);
        }
        if (questionNo < 10) {
            questionNumber.setText("0" + questionNo);
        } else {
            questionNumber.setText(questionNo);
        }
        if (questionNo == totalQuestions) {
            doLater.setVisibility(View.INVISIBLE);
            doLater.setClickable(false);

        }

        databaseRef();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("call", "calling");
                Log.d("Question", String.valueOf(snapshot.getChildrenCount()));
                question.setText(Objects.requireNonNull(snapshot.child("q").getValue()).toString());
                correctAnswer = Objects.requireNonNull(snapshot.child("a").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("call", "No Calling");
            }
        });
    }

    private void databaseRef() {
        if (lang.equals("en")) {
            medium = "English";
            myRef = database.getReference("mcq").child(medium).child(selectedSubject).child(selectedYear).child(questionNo.toString());
        } else {
            if (lang.equals("si")) {
                medium = "Sinhala";
                myRef = database.getReference("mcq").child(medium).child(selectedYear).child(selectedSubject).child(questionNo.toString());
            } else {
                if (lang.equals("ta")) {
                    medium = "Tamil";
                    myRef = database.getReference("mcq").child(medium).child(selectedYear).child(selectedSubject).child(questionNo.toString());
                }
            }
        }
    }

    private void checkQuestions() {
        if (lang.equals("en")) {
            ref = database.getReference("mcq").child(medium).child(selectedSubject).child(selectedYear);
        } else {
            ref = database.getReference("mcq").child(medium).child(selectedYear).child(selectedSubject);
        }
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalQuestions = Math.toIntExact(snapshot.getChildrenCount());
                Log.d("Size", String.valueOf(snapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("call", "No Calling");
            }
        });

    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(durationMillisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                durationMillisecond = millisUntilFinished;

                int min = (int) durationMillisecond / 60000;

                moreTime=120-min;

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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),SelectPaperActivity.class));
        finish();
    }
}