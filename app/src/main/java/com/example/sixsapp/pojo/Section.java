package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Section {
    @SerializedName("SectionID")
    @Expose
    private int SectionID;

    @SerializedName("SectionName")
    @Expose
    private String SectionName;

    public Section(int sectionID, String sectionName) {
        SectionID = sectionID;
        SectionName = sectionName;
    }

    public int getSectionID() {
        return SectionID;
    }

    public void setSectionID(int sectionID) {
        SectionID = sectionID;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }
    @Override
    public String toString() {
        return SectionName;
    }
}
