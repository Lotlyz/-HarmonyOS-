package com.example.lotday2.bean;

import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;

public class Diary {
    private int diaryId;

    private String diaryTitle;

    private String diaryContent;

    private String diaryImage;

    private int diaryDateYear;

    private int diaryDateMonth;

    private int diaryDateDay;

    private int diaryDiarybookId;

    private int diaryUserId;

    public Diary() {
    }

    public Diary(int diaryId, String diaryTitle, String diaryContent, String diaryImage, int diaryDateYear, int diaryDateMonth, int diaryDateDay, int diaryDiarybookId, int diaryUserId) {
        this.diaryId = diaryId;
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.diaryImage = diaryImage;
        this.diaryDateYear = diaryDateYear;
        this.diaryDateMonth = diaryDateMonth;
        this.diaryDateDay = diaryDateDay;
        this.diaryDiarybookId = diaryDiarybookId;
        this.diaryUserId = diaryUserId;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public String getDiaryTitle() {
        return diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

    public String getDiaryImage() {
        return diaryImage;
    }

    public void setDiaryImage(String diaryImage) {
        this.diaryImage = diaryImage;
    }

    public int getDiaryDateYear() {
        return diaryDateYear;
    }

    public void setDiaryDateYear(int diaryDateYear) {
        this.diaryDateYear = diaryDateYear;
    }

    public int getDiaryDateMonth() {
        return diaryDateMonth;
    }

    public void setDiaryDateMonth(int diaryDateMonth) {
        this.diaryDateMonth = diaryDateMonth;
    }

    public int getDiaryDateDay() {
        return diaryDateDay;
    }

    public void setDiaryDateDay(int diaryDateDay) {
        this.diaryDateDay = diaryDateDay;
    }

    public int getDiaryDiarybookId() {
        return diaryDiarybookId;
    }

    public void setDiaryDiarybookId(int diaryDiarybookId) {
        this.diaryDiarybookId = diaryDiarybookId;
    }

    public int getDiaryUserId() {
        return diaryUserId;
    }

    public void setDiaryUserId(int diaryUserId) {
        this.diaryUserId = diaryUserId;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "diaryId=" + diaryId +
                ", diaryTitle='" + diaryTitle + '\'' +
                ", diaryContent='" + diaryContent + '\'' +
                ", diaryImage='" + diaryImage + '\'' +
                ", diaryDateYear=" + diaryDateYear +
                ", diaryDateMonth=" + diaryDateMonth +
                ", diaryDateDay=" + diaryDateDay +
                ", diaryDiarybookId=" + diaryDiarybookId +
                ", diaryUserId=" + diaryUserId +
                '}';
    }

    private static RdbOpenCallback DiaryCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_diary (diary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "diary_title varchar(100),diary_content varchar(500), diary_date varchar(100),diary_author varchar(100) )");
        }
        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    public static RdbOpenCallback getDiaryCallback() {
        return DiaryCallback;
    }

    public static void setDiaryCallback(RdbOpenCallback diaryCallback) {
        DiaryCallback = diaryCallback;
    }
}
