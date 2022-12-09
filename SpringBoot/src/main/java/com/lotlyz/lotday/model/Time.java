package com.lotlyz.lotday.model;

import java.io.Serializable;

public class Time implements Serializable {
    private Integer timeId;

    private String timeTimeline;

    private String timeType;

    private String timeDuration;

    private String timeRemarks;

    private String timeDate;

    private Integer timeUserId;

    private static final long serialVersionUID = 1L;

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
    }

    public String getTimeTimeline() {
        return timeTimeline;
    }

    public void setTimeTimeline(String timeTimeline) {
        this.timeTimeline = timeTimeline;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getTimeRemarks() {
        return timeRemarks;
    }

    public void setTimeRemarks(String timeRemarks) {
        this.timeRemarks = timeRemarks;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public Integer getTimeUserId() {
        return timeUserId;
    }

    public void setTimeUserId(Integer timeUserId) {
        this.timeUserId = timeUserId;
    }
}