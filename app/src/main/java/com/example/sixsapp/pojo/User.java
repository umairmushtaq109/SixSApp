package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("AccountID")
    @Expose
    public int AccountID;

    @SerializedName("EmployeeID")
    @Expose
    public int EmployeeID;

    @SerializedName("CardNo")
    @Expose
    public int CardNo;

    @SerializedName("EmployeeName")
    @Expose
    public String EmployeeName;

    @SerializedName("Pin")
    @Expose
    public int Pin;

    @SerializedName("Category")
    @Expose
    public String Category;

    public User(int accountID, int employeeID, int cardNo, String employeeName, int pin, String category) {
        AccountID = accountID;
        EmployeeID = employeeID;
        CardNo = cardNo;
        EmployeeName = employeeName;
        Pin = pin;
        Category = category;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public int getCardNo() {
        return CardNo;
    }

    public void setCardNo(int cardNo) {
        CardNo = cardNo;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
