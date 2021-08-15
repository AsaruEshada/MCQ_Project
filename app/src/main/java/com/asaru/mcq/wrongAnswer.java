package com.asaru.mcq;

import android.os.Parcel;
import android.os.Parcelable;

public class wrongAnswer implements Parcelable {
    Integer questionNo;
    String wrongAnswer,correctAnswer,questionImage;

    public wrongAnswer() {
    }

    public wrongAnswer(Integer questionNo, String wrongAnswer, String correctAnswer, String questionImage) {
        this.questionNo = questionNo;
        this.wrongAnswer = wrongAnswer;
        this.correctAnswer = correctAnswer;
        this.questionImage = questionImage;
    }

    protected wrongAnswer(Parcel in) {
        if (in.readByte() == 0) {
            questionNo = null;
        } else {
            questionNo = in.readInt();
        }
        wrongAnswer = in.readString();
        correctAnswer = in.readString();
        questionImage = in.readString();
    }

    public static final Creator<wrongAnswer> CREATOR = new Creator<wrongAnswer>() {
        @Override
        public wrongAnswer createFromParcel(Parcel in) {
            return new wrongAnswer(in);
        }

        @Override
        public wrongAnswer[] newArray(int size) {
            return new wrongAnswer[size];
        }
    };

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.questionNo);
        dest.writeString(this.wrongAnswer);
        dest.writeString(this.correctAnswer);
        dest.writeString(this.questionImage);
    }
}
