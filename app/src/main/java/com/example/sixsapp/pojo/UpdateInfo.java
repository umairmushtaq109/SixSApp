package com.example.sixsapp.pojo;

import com.google.gson.annotations.SerializedName;

public class UpdateInfo {
    @SerializedName("versionCode")
    private int versionCode;
    @SerializedName("versionName")
    private String versionName;
    @SerializedName("releaseNotes")
    private String releaseNotes;

    @SerializedName("url")
    private String downloadUrl;

    public UpdateInfo(int versionCode, String versionName, String releaseNotes, String downloadUrl) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.releaseNotes = releaseNotes;
        this.downloadUrl = downloadUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
