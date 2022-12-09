package com.lotlyz.lotday.model;

import java.io.Serializable;

public class Diarybook implements Serializable {
    private Integer diarybookId;

    private String diarybookName;

    private String diarybookImage;

    private String diarybookType;

    private Integer diarybookUserId;

    private static final long serialVersionUID = 1L;

    public Integer getDiarybookId() {
        return diarybookId;
    }

    public void setDiarybookId(Integer diarybookId) {
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

    public Integer getDiarybookUserId() {
        return diarybookUserId;
    }

    public void setDiarybookUserId(Integer diarybookUserId) {
        this.diarybookUserId = diarybookUserId;
    }
}