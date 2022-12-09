package com.example.lotday2.bean;

import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;

public class Mood {

//    private int mood_id;
//    private String mood_image;
//    private String mood_sheying;
//    private String mood_sheyingzhe;
//    private String mood_mood;
//    private String mood_date;
//    private String mood_address;
//    private String mood_author;
//    private int mood_user_id;

    private int moodId;

    private String moodImage;

    private String moodSheying;

    private String moodSheyingzhe;

    private String moodMood;

    private String moodDate;

    private String moodAddress;

    private String moodAuthor;

    private int moodUserId;

    public Mood() {
    }

    public Mood(int moodId, String moodImage, String moodSheying, String moodSheyingzhe, String moodMood, String moodDate, String moodAddress, String moodAuthor, int moodUserId) {
        this.moodId = moodId;
        this.moodImage = moodImage;
        this.moodSheying = moodSheying;
        this.moodSheyingzhe = moodSheyingzhe;
        this.moodMood = moodMood;
        this.moodDate = moodDate;
        this.moodAddress = moodAddress;
        this.moodAuthor = moodAuthor;
        this.moodUserId = moodUserId;
    }

    public int getMoodId() {
        return moodId;
    }

    public void setMoodId(int moodId) {
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

    public int getMoodUserId() {
        return moodUserId;
    }

    public void setMoodUserId(int moodUserId) {
        this.moodUserId = moodUserId;
    }

    @Override
    public String toString() {
        return "Mood{" +
                "moodId=" + moodId +
                ", moodImage='" + moodImage + '\'' +
                ", moodSheying='" + moodSheying + '\'' +
                ", moodSheyingzhe='" + moodSheyingzhe + '\'' +
                ", moodMood='" + moodMood + '\'' +
                ", moodDate='" + moodDate + '\'' +
                ", moodAddress='" + moodAddress + '\'' +
                ", moodAuthor='" + moodAuthor + '\'' +
                ", moodUserId=" + moodUserId +
                '}';
    }

    private static RdbOpenCallback MoodCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_mood (mood_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "mood_image varchar(100),mood_sheying varchar(100), mood_sheyingzhe varchar(100)," +
                    "mood_mood varchar(500),mood_date varchar(100),mood_address varchar(100), mood_author varchar(100) )");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    public static RdbOpenCallback getMoodCallback() {
        return MoodCallback;
    }

    public static void setMoodCallback(RdbOpenCallback moodCallback) {
        MoodCallback = moodCallback;
    }
}
