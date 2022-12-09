package com.lotlyz.lotday.model;

import java.io.Serializable;

public class Diary implements Serializable {
    private Integer diaryId;

    private String diaryTitle;

    private String diaryContent;

    private String diaryImage;

    private Integer diaryDateYear;

    private Integer diaryDateMonth;

    private Integer diaryDateDay;

    private Integer diaryDiarybookId;

    private Integer diaryUserId;

    private static final long serialVersionUID = 1L;

    public Integer getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Integer diaryId) {
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

    public Integer getDiaryDateYear() {
        return diaryDateYear;
    }

    public void setDiaryDateYear(Integer diaryDateYear) {
        this.diaryDateYear = diaryDateYear;
    }

    public Integer getDiaryDateMonth() {
        return diaryDateMonth;
    }

    public void setDiaryDateMonth(Integer diaryDateMonth) {
        this.diaryDateMonth = diaryDateMonth;
    }

    public Integer getDiaryDateDay() {
        return diaryDateDay;
    }

    public void setDiaryDateDay(Integer diaryDateDay) {
        this.diaryDateDay = diaryDateDay;
    }

    public Integer getDiaryDiarybookId() {
        return diaryDiarybookId;
    }

    public void setDiaryDiarybookId(Integer diaryDiarybookId) {
        this.diaryDiarybookId = diaryDiarybookId;
    }

    public Integer getDiaryUserId() {
        return diaryUserId;
    }

    public void setDiaryUserId(Integer diaryUserId) {
        this.diaryUserId = diaryUserId;
    }
}