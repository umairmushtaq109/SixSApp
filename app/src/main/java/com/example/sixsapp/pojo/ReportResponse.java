package com.example.sixsapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportResponse {
    @SerializedName("ResultID")
    @Expose
    public int ResultID;

    @SerializedName("AuditDate")
    @Expose
    public String AuditDate;

    @SerializedName("DepartmentID")
    @Expose
    public int DepartmentID;

    @SerializedName("SectionID")
    @Expose
    public int SectionID;

    @SerializedName("DeptName")
    @Expose
    public String DeptName;

    @SerializedName("SectionName")
    @Expose
    public String SectionName;

    @SerializedName("SubmittedDateTime")
    @Expose
    public String SubmittedDateTime;

    @SerializedName("Remarks")
    @Expose
    public String Remarks;

    public ReportResponse(int resultID, String auditDate, int departmentID, int sectionID, String deptName, String sectionName, String submittedDateTime, String remarks) {
        ResultID = resultID;
        AuditDate = auditDate;
        DepartmentID = departmentID;
        SectionID = sectionID;
        DeptName = deptName;
        SectionName = sectionName;
        SubmittedDateTime = submittedDateTime;
        Remarks = remarks;
    }

    public int getResultID() {
        return ResultID;
    }

    public void setResultID(int resultID) {
        ResultID = resultID;
    }

    public String getAuditDate() {
        return AuditDate;
    }

    public void setAuditDate(String auditDate) {
        AuditDate = auditDate;
    }

    public int getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(int departmentID) {
        DepartmentID = departmentID;
    }

    public int getSectionID() {
        return SectionID;
    }

    public void setSectionID(int sectionID) {
        SectionID = sectionID;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getSubmittedDateTime() {
        return SubmittedDateTime;
    }

    public void setSubmittedDateTime(String submittedDateTime) {
        SubmittedDateTime = submittedDateTime;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
