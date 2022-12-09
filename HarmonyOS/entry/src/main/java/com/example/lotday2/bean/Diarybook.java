package com.example.lotday2.bean;

import ohos.data.rdb.RdbOpenCallback;
import ohos.data.rdb.RdbStore;

public class Diarybook {
    private int diarybookId;

    private String diarybookName;

    private String diarybookImage;

    private String diarybookType;

    private int diarybookUserId;

    public Diarybook() {
    }

    public Diarybook(int diarybookId, String diarybookName, String diarybookImage, String diarybookType, int diarybookUserId) {
        this.diarybookId = diarybookId;
        this.diarybookName = diarybookName;
        this.diarybookImage = diarybookImage;
        this.diarybookType = diarybookType;
        this.diarybookUserId = diarybookUserId;
    }

    public int getDiarybookId() {
        return diarybookId;
    }

    public void setDiarybookId(int diarybookId) {
        this.diarybookId = diarybookId;
    }

    public String getDiarybookName() {
        return diarybookName;
    }

    public void setDiarybookName(String diarybookName) {
        this.diarybookName = diarybookName;
    }

    public String getDiarybookImage() {
        return diarybookImage;
    }

    public void setDiarybookImage(String diarybookImage) {
        this.diarybookImage = diarybookImage;
    }

    public String getDiarybookType() {
        return diarybookType;
    }

    public void setDiarybookType(String diarybookType) {
        this.diarybookType = diarybookType;
    }

    public int getDiarybookUserId() {
        return diarybookUserId;
    }

    public void setDiarybookUserId(int diarybookUserId) {
        this.diarybookUserId = diarybookUserId;
    }

    @Override
    public String toString() {
        return "Diarybook{" +
                "diarybookId=" + diarybookId +
                ", diarybookName='" + diarybookName + '\'' +
                ", diarybookImage='" + diarybookImage + '\'' +
                ", diarybookType='" + diarybookType + '\'' +
                ", diarybookUserId=" + diarybookUserId +
                '}';
    }

    private static RdbOpenCallback DiaryBookCallback = new RdbOpenCallback() {
        @Override
        public void onCreate(RdbStore store) {
            store.executeSql("create table if not exists yz_diarybook (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "diarybookname varchar(100))");
        }

        @Override
        public void onUpgrade(RdbStore store, int oldVersion, int newVersion) {
        }
    };

    public static RdbOpenCallback getDiaryBookCallback() {
        return DiaryBookCallback;
    }

    public static void setDiaryBookCallback(RdbOpenCallback diaryBookCallback) {
        DiaryBookCallback = diaryBookCallback;
    }
}
