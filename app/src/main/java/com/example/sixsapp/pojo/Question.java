package com.example.sixsapp.pojo;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("CategoryID")
    @Expose
    public int CategoryID;

    @SerializedName("QuestionID")
    @Expose
    public int QuestionID;

    @SerializedName("Question")
    @Expose
    public String Question;

    public Question(int categoryID, int questionID, String question) {
        CategoryID = categoryID;
        QuestionID = questionID;
        Question = question;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }
}
