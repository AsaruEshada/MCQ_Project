package com.asaru.mcq;

import android.os.Parcel;
import android.os.Parcelable;

public class correctAnswer implements Parcelable {


    Integer questionNo;
    String answer;
    String questionImage;

    public correctAnswer() {
    }

    public correctAnswer(Integer questionNo, String answer, String questionImage) {
        this.questionNo = questionNo;
        this.answer = answer;
        this.questionImage = questionImage;
    }

    protected correctAnswer(Parcel in) {
        if (in.readByte() == 0) {
            questionNo = null;
        } else {
            questionNo = in.readInt();
        }
        answer = in.readString();
        questionImage = in.readString();
    }

    public static final Creator<correctAnswer> CREATOR = new Creator<correctAnswer>() {
        @Override
        public correctAnswer createFromParcel(Parcel in) {
            return new correctAnswer(in);
        }

        @Override
        public correctAnswer[] newArray(int size) {
            return new correctAnswer[size];
        }
    };

    public Integer getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(Integer questionNo) {
        this.questionNo = questionNo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
        if (questionNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(questionNo);
        }
        dest.writeString(answer);
        dest.writeString(questionImage);
    }
}
