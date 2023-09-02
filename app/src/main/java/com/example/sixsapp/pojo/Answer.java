package com.example.sixsapp.pojo;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

public class Answer {
    @SerializedName("CategoryID")
    @Expose
    public int CategoryID;

    @SerializedName("QuestionID")
    @Expose
    public int QuestionID;

    @SerializedName("Findings")
    @Expose
    public int Findings = 0;

    @SerializedName("ResultID")
    @Expose
    public int ResultID;

    @SerializedName("Remarks")
    @Expose
    public String Remarks = "";

    @SerializedName("ImageBase64")
    @Expose
    public List<String> ImageBase64;

//    @SerializedName("ImagesBytes")
//    @Expose
    public List<byte[]> ImagesBytes = new ArrayList<>();

    public Bitmap[] ImageBitmap = new Bitmap[5];

    public Uri[] ImageUris = new Uri[5];

    public Answer() {
    }

    public Answer(int categoryID, int questionID, int Findings, int ResultID, String remarks, List<String> imageBase64, List<byte[]> ImagesBytes) {
        this.CategoryID = categoryID;
        this.QuestionID = questionID;
        this.Findings = Findings;
        this.ResultID = ResultID;
        this.Remarks = remarks;
        this.ImageBase64 = imageBase64;
        this.ImagesBytes = ImagesBytes;
    }


    public List<String> getImageBase64() {
        return ImageBase64;
    }

    public void setImageBase64(List<String> imageBase64) {
        ImageBase64 = imageBase64;
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

    public int getFindings() {
        return Findings;
    }

    public void setFindings(int findings) {
        Findings = findings;
    }

    public Bitmap[] getImageBitmap() {
        return ImageBitmap;
    }

    public void setImageBitmap(Bitmap[] imageBitmap) {
        ImageBitmap = imageBitmap;
    }

    public Uri[] getImageUris() {
        return ImageUris;
    }

    public void setImageUris(Uri[] imageUris) {
        ImageUris = imageUris;
    }

    public List<byte[]> getImagesBytes() {
        return ImagesBytes;
    }

    public void setImagesBytes(List<byte[]> imagesBytes) {
        ImagesBytes = imagesBytes;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getResultID() {
        return ResultID;
    }

    public void setResultID(int resultID) {
        ResultID = resultID;
    }
}
