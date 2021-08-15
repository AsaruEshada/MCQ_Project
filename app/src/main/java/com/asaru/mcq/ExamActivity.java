package com.asaru.mcq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
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

    String selectedYear;
    String selectedSubject;
    String lang;
    String medium;
    String correctAnswer;
    String questionUrl;
    String totalQuestionNo;
    TextView subject, year, exit, questionView, time, questionNumber, doneQuestions;
    FirebaseDatabase database;
    DatabaseReference myRef, ref;
    Button next, back, doLater, finish;
    Integer questionNo = 1;
    Integer realQuestionNo = 1;
    Integer doLaterTotal = 0, answerTotalQuestion = 0, correctAnswerNo = 0, wrongAnswerNo = 0, doLaterEqual = 0;
    CharSequence userAnswer;
    RadioGroup radioGroup;
    Integer totalQuestions, moreTime;
    private CountDownTimer countDownTimer;
    private long durationMillisecond = 600000 * 12;
    private boolean timerRunning;
    private doLater doLaterQuestion;
    ArrayList<Integer> doLaterQuestions = new ArrayList<>();
    ArrayList<Integer> correctQuestionNo = new ArrayList<>();
    ArrayList<doLater> doLaterArray = new ArrayList<>();
    ArrayList<wrongAnswer> wrongQuestions = new ArrayList<>();
    ArrayList<correctAnswer> correctQuestions = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exam);
        subject = findViewById(R.id.subject);
        year = findViewById(R.id.year);
        exit = findViewById(R.id.exit);
        questionView = findViewById(R.id.question);
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
        doneQuestions = findViewById(R.id.doneQuestion);

        exit.setOnClickListener(v ->
        {
            startActivity(new Intent(getApplicationContext(), SelectPaperActivity.class));
            finish();
        });
        database = FirebaseDatabase.getInstance();
        SharedPreferences language = getApplicationContext().getSharedPreferences("language", 0);
        lang = language.getString("Language", "");

        updateQuestion(questionNo);
        checkQuestions();
        //countdown timer
        startTimer();


        next.setOnClickListener(v -> {

            if (timerRunning) {

                if (questionNo.equals(totalQuestions)) {

                    if (doLaterTotal.equals(0)) {

                        countDownTimer.cancel();

                        Intent i = new Intent(getApplicationContext(), ExamFinishActivity.class);
                        i.putExtra("time", moreTime);
                        // i.putExtra("wrong", wrongAnswerNo);
                        //i.putExtra("correct", correctAnswerNo);
                        //i.putExtra("wrongAnswer", wrongQuestionNo);
                        // i.putExtra("correctAnswer", correctQuestionNo);
                        // i.putExtra("correctQuestionsUrl", correctQuestionsUrl);
                        // i.putExtra("wrongQuestionsUrl", wrongQuestionsUrl);
                        i.putParcelableArrayListExtra("correctAnswerArray", correctQuestions);
                        i.putParcelableArrayListExtra("wrongAnswerArray", wrongQuestions);

                        startActivity(i);
                        finish();

                    } else {
                        doLater.setVisibility(View.INVISIBLE);
                        if (doLaterTotal == 0) {
                            countDownTimer.cancel();

                            Intent i = new Intent(getApplicationContext(), ExamFinishActivity.class);
                            i.putExtra("time", moreTime);
                            // i.putExtra("wrong", wrongAnswerNo);
                            //i.putExtra("correct", correctAnswerNo);
                            //i.putExtra("wrongAnswer", wrongQuestionNo);
                            // i.putExtra("correctAnswer", correctQuestionNo);
                            // i.putExtra("correctQuestionsUrl", correctQuestionsUrl);
                            // i.putExtra("wrongQuestionsUrl", wrongQuestionsUrl);
                            i.putParcelableArrayListExtra("correctAnswerArray", correctQuestions);
                            i.putParcelableArrayListExtra("wrongAnswerArray", wrongQuestions);

                            startActivity(i);
                            finish();
                        } else {
                            realQuestionNo = doLaterQuestions.get(doLaterTotal - 1);
                            Log.d("Do later", doLaterQuestions.get(doLaterTotal - 1).toString());
                            doLaterEqual++;
                            updateQuestion(doLaterQuestions.get(doLaterTotal - 1));
                            checkAnswerDoLater(realQuestionNo);


                        }


                    }

                } else {
                    checkAnswer();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Your time is over", Toast.LENGTH_LONG).show();
            }
        });
        doLater.setOnClickListener(v -> {
            doLaterQuestions.add(doLaterTotal, questionNo);
            //doLaterQuestion = new doLater(questionNo, correctAnswer, questionUrl);
            //doLaterArray.add(doLaterTotal, doLaterQuestion);
            doLaterTotal++;
            questionNo++;
            realQuestionNo++;
            updateQuestion(questionNo);
        });
        back.setOnClickListener(v -> {
            if (questionNo.equals(totalQuestions)) {
                if (doLaterEqual > 0) {
                    if (doLaterTotal > 0) {
                        if (correctQuestions.size() > 0) {
                            if (correctQuestions.get(correctAnswerNo - 1).questionNo.equals(doLaterQuestions.get(doLaterTotal))) {
                                correctQuestions.remove(correctAnswerNo - 1);
                                correctAnswerNo = correctAnswerNo - 1;
                            } else {
                                if (wrongQuestions.get(wrongAnswerNo - 1).questionNo.equals(doLaterQuestions.get(doLaterTotal))) {

                                    wrongQuestions.remove(wrongAnswerNo - 1);
                                    wrongAnswerNo = wrongAnswerNo - 1;

                                }
                            }
                            updateQuestion(doLaterQuestions.get(doLaterTotal));
                        }
                    }else {Toast.makeText(getApplicationContext(), "You cant go back ", Toast.LENGTH_LONG).show();}
                } else {
                    Toast.makeText(getApplicationContext(), "You cant go back ", Toast.LENGTH_LONG).show();
                }


            } else {
                if (correctQuestions.size() > 0) {
                    if (correctQuestions.get(correctAnswerNo - 1).questionNo.equals(questionNo - 1)) {
                        correctQuestions.remove(correctAnswerNo - 1);
                        correctAnswerNo = correctAnswerNo - 1;
                    }
                }
                if (wrongQuestions.size() > 0) {
                    if (wrongQuestions.get(wrongAnswerNo - 1).questionNo.equals(questionNo - 1)) {

                        wrongQuestions.remove(wrongAnswerNo - 1);
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

                updateQuestion(questionNo);
            }


        });
        finish.setOnClickListener(v -> {
            if (questionNo.equals(totalQuestions)) {

                Intent i = new Intent(getApplicationContext(), ExamFinishActivity.class);
                /*i.putExtra("time", moreTime);
                i.putExtra("wrong", wrongAnswerNo);
                i.putExtra("correct", correctAnswerNo);
                i.putExtra("wrongAnswer", wrongQuestionNo);
                i.putExtra("correctAnswer", correctQuestionNo);*/
                i.putExtra("correctAnswerArray", (Parcelable) correctQuestions);
                i.putExtra("wrongAnswerArray", (Parcelable) wrongQuestions);


                startActivity(i);
                finish();

            }
            if (questionNo < 2) {
                startActivity(new Intent(getApplicationContext(), SelectPaperActivity.class));
                finish();
            }
            if (questionNo > 2) {
                Intent i = new Intent(getApplicationContext(), ExamFinishActivity.class);
                /*i.putExtra("time", moreTime);
                i.putExtra("wrong", wrongAnswerNo);
                i.putExtra("correct", correctAnswerNo);
                i.putExtra("wrongAnswer", wrongQuestionNo);
                i.putExtra("correctAnswer", correctQuestionNo);*/
                i.putExtra("correctAnswerArray", (Parcelable) correctQuestions);
                i.putExtra("wrongAnswerArray", (Parcelable) wrongQuestions);


                startActivity(i);
                finish();

            }
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void updateQuestion(int question) {
        if (questionNo > 1) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.INVISIBLE);
        }
        if (realQuestionNo < 10) {
            questionNumber.setText("0" + realQuestionNo);
        } else {
            questionNumber.setText(realQuestionNo);
        }
        if (questionNo.equals(totalQuestions)) {

            doLater.setVisibility(View.INVISIBLE);
            doLater.setClickable(false);

        }


        databaseRef(question);
        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("call", "calling");
                Log.d("Question", String.valueOf(snapshot.getChildrenCount()));
                totalQuestionNo = String.valueOf(snapshot.getChildrenCount());
                questionView.setText(Objects.requireNonNull(snapshot.child("q").getValue()).toString());
                Log.d("URL", Objects.requireNonNull(snapshot.child("q").getValue()).toString());
                questionUrl = Objects.requireNonNull(snapshot.child("q").getValue()).toString();
                correctAnswer = Objects.requireNonNull(snapshot.child("a").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("call", "No Calling");
            }
        });

    }

    private void databaseRef(Integer question) {
        if (lang.equals("en")) {
            medium = "English";
            myRef = database.getReference("mcq").child(medium).child(selectedSubject).child(selectedYear).child((question).toString());
        } else {
            if (lang.equals("si")) {
                medium = "Sinhala";
                myRef = database.getReference("mcq").child(medium).child(selectedYear).child(selectedSubject).child(question.toString());
            } else {
                if (lang.equals("ta")) {
                    medium = "Tamil";
                    myRef = database.getReference("mcq").child(medium).child(selectedYear).child(selectedSubject).child(question.toString());
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
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalQuestions = Math.toIntExact(snapshot.getChildrenCount());
                doneQuestions.setText(answerTotalQuestion + "/" + (totalQuestions - 1));
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

                moreTime = 120 - min;

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
        startActivity(new Intent(getApplicationContext(), SelectPaperActivity.class));
        finish();
    }

    public void checkAnswer() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_LONG).show();
        } else {
            int radioId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioId);
            userAnswer = radioButton.getText();
            //Toast.makeText(getApplicationContext(), userAnswer, Toast.LENGTH_SHORT).show();
            if (userAnswer.equals(correctAnswer)) {
                com.asaru.mcq.correctAnswer correctAnswerArray = new correctAnswer(questionNo, correctAnswer, questionUrl);
                //correctQuestionNo.add(correctAnswerNo, questionNo);
                // correctQuestionsUrl.add(correctAnswerNo, questionUrl);
                correctQuestions.add(correctAnswerNo, correctAnswerArray);
                // Toast.makeText(getApplicationContext(), correctAnswerArray.toString(), Toast.LENGTH_SHORT).show();
                correctAnswerNo++;

            } else {
                wrongAnswer wrongAnswerArray = new wrongAnswer(questionNo, (String) userAnswer, correctAnswer, questionUrl);
                //wrongQuestionNo.add(wrongAnswerNo, questionNo);
                //wrongQuestionsUrl.add(wrongAnswerNo, questionUrl);
                wrongQuestions.add(wrongAnswerNo, wrongAnswerArray);
                //Toast.makeText(getApplicationContext(), wrongAnswerArray.toString(), Toast.LENGTH_SHORT).show();
                wrongAnswerNo++;

            }
            questionNo++;
            realQuestionNo++;
            answerTotalQuestion++;
            radioGroup.clearCheck();
            updateQuestion(questionNo);

        }
    }

    public void checkAnswerDoLater(int question) {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select one answer", Toast.LENGTH_LONG).show();
        } else {
            int radioId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioId);
            userAnswer = radioButton.getText();
            //Toast.makeText(getApplicationContext(), userAnswer, Toast.LENGTH_SHORT).show();
            if (userAnswer.equals(correctAnswer)) {
                com.asaru.mcq.correctAnswer correctAnswerArray = new correctAnswer(question, correctAnswer, questionUrl);
                //correctQuestionNo.add(correctAnswerNo, questionNo);
                // correctQuestionsUrl.add(correctAnswerNo, questionUrl);
                correctQuestions.add(correctAnswerNo, correctAnswerArray);
                // Toast.makeText(getApplicationContext(), correctAnswerArray.toString(), Toast.LENGTH_SHORT).show();
                correctAnswerNo++;

            } else {
                wrongAnswer wrongAnswerArray = new wrongAnswer(question, (String) userAnswer, correctAnswer, questionUrl);
                //wrongQuestionNo.add(wrongAnswerNo, questionNo);
                //wrongQuestionsUrl.add(wrongAnswerNo, questionUrl);
                wrongQuestions.add(wrongAnswerNo, wrongAnswerArray);
                //Toast.makeText(getApplicationContext(), wrongAnswerArray.toString(), Toast.LENGTH_SHORT).show();
                wrongAnswerNo++;

            }
            doLaterTotal--;

            answerTotalQuestion++;
            radioGroup.clearCheck();
            updateQuestion(question);
        }
    }
}