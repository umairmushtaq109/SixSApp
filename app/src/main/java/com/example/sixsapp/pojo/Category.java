package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("CategoryID")
    @Expose
    public int CategoryID;

    @SerializedName("CategoryName")
    @Expose
    public String CategoryName;

    @SerializedName("CategoryColor")
    @Expose
    public String CategoryColor;

    public boolean isDisable = true;

    public Category(int categoryID, String categoryName, String categoryColor) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        CategoryColor = categoryColor;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryColor() {
        return CategoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        CategoryColor = categoryColor;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean enabled) {
        isDisable = enabled;
    }
}
