package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName("DepartmentID")
    @Expose
    private int DepartmentID;

    @SerializedName("DeparmentName")
    @Expose
    private String DepartmentName;

    public Department(int departmentID, String departmentName) {
        DepartmentID = departmentID;
        DepartmentName = departmentName;
    }

    public int getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(int departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    @Override
    public String toString() {
        return DepartmentName;
    }
}
