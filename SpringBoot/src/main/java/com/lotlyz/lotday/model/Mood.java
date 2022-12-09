package com.lotlyz.lotday.model;

import java.io.Serializable;

public class Mood implements Serializable {
    private Integer moodId;

    private String moodImage;

    private String moodSheying;

    private String moodSheyingzhe;

    private String moodMood;

    private String moodDate;

    private String moodAddress;

    private String moodAuthor;

    private Integer moodUserId;

    private static final long serialVersionUID = 1L;

    public Integer getMoodId() {
        return moodId;
    }

    public void setMoodId(Integer moodId) {
        this.moodId = moodId;
    }

    public String getMoodImage() {
        return moodImage;
    }

    public void setMoodImage(String moodImage) {
        this.moodImage = moodImage;
    }

    public String getMoodSheying() {
        return moodSheying;
    }

    public void setMoodSheying(String moodSheying) {
        this.moodSheying = moodSheying;
    }

    public String getMoodSheyingzhe() {
        return moodSheyingzhe;
    }

    public void setMoodSheyingzhe(String moodSheyingzhe) {
        this.moodSheyingzhe = moodSheyingzhe;
    }

    public String getMoodMood() {
        return moodMood;
    }

    public void setMoodMood(String moodMood) {
        this.moodMood = moodMood;
    }

    public String getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(String moodDate) {
        this.moodDate = moodDate;
    }

    public String getMoodAddress() {
        return moodAddress;
    }

    public void setMoodAddress(String moodAddress) {
        this.moodAddress = moodAddress;
    }

    public String getMoodAuthor() {
        return moodAuthor;
    }

    public void setMoodAuthor(String moodAuthor) {
        this.moodAuthor = moodAuthor;
    }

    public Integer getMoodUserId() {
        return moodUserId;
    }

    public void setMoodUserId(Integer moodUserId) {
        this.moodUserId = moodUserId;
    }
}