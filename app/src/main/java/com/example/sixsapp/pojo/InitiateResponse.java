package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitiateResponse {
    @SerializedName("ResultID")
    @Expose
    private int ResultID;
    @SerializedName("Sort")
    @Expose
    private int Sort;
    @SerializedName("SetInOrder")
    @Expose
    private int SetInOrder;
    @SerializedName("Safety")
    @Expose
    private int Safety;
    @SerializedName("Shine")
    @Expose
    private int Shine;
    @SerializedName("Standard")
    @Expose
    private int Standard;
    @SerializedName("Sustain")
    @Expose
    private int Sustain;

    @SerializedName("IsSubmitted")
    @Expose
    private int IsSubmitted;

    public InitiateResponse(int resultID, int sort, int setInOrder, int safety, int shine, int standard, int sustain, int isSubmitted) {
        ResultID = resultID;
        Sort = sort;
        SetInOrder = setInOrder;
        Safety = safety;
        Shine = shine;
        Standard = standard;
        Sustain = sustain;
        IsSubmitted = isSubmitted;
    }

    public int getResultID() {
        return ResultID;
    }

    public void setResultID(int resultID) {
        ResultID = resultID;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public int getSetInOrder() {
        return SetInOrder;
    }

    public void setSetInOrder(int setInOrder) {
        SetInOrder = setInOrder;
    }

    public int getSafety() {
        return Safety;
    }

    public void setSafety(int safety) {
        Safety = safety;
    }

    public int getShine() {
        return Shine;
    }

    public void setShine(int shine) {
        Shine = shine;
    }

    public int getStandard() {
        return Standard;
    }

    public void setStandard(int standard) {
        Standard = standard;
    }

    public int getSustain() {
        return Sustain;
    }

    public void setSustain(int sustain) {
        Sustain = sustain;
    }

    public int getIsSubmitted() {
        return IsSubmitted;
    }

    public void setIsSubmitted(int isSubmitted) {
        IsSubmitted = isSubmitted;
    }
}
