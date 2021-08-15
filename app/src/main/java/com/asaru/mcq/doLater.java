package com.asaru.mcq;

public class doLater {
    Integer question;
    String answer,questionImage;

    public doLater(Integer question, String answer, String questionImage) {
        this.question = question;
        this.answer = answer;
        this.questionImage = questionImage;
    }

    public Integer getQuestion() {
        return question;
    }

    public void setQuestion(Integer question) {
        this.question = question;
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
}
