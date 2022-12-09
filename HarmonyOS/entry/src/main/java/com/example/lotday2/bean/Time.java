package com.example.lotday2.bean;

public class Time {
//    private int time_id;
//    private String time_timeline;
//    private String time_type;
//    private String time_duration;
//    private String time_remarks;
//    private String time_date;
//    private int time_user_id ;

    private Integer timeId;

    private String timeTimeline;

    private String timeType;

    private String timeDuration;

    private String timeRemarks;

    private String timeDate;

    private Integer timeUserId;

    public Time() {
    }

    public Time(Integer timeId, String timeTimeline, String timeType, String timeDuration, String timeRemarks, String timeDate, Integer timeUserId) {
        this.timeId = timeId;
        this.timeTimeline = timeTimeline;
        this.timeType = timeType;
        this.timeDuration = timeDuration;
        this.timeRemarks = timeRemarks;
        this.timeDate = timeDate;
        this.timeUserId = timeUserId;
    }

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

    @Override
    public String toString() {
        return "Time{" +
                "timeId=" + timeId +
                ", timeTimeline='" + timeTimeline + '\'' +
                ", timeType='" + timeType + '\'' +
                ", timeDuration='" + timeDuration + '\'' +
                ", timeRemarks='" + timeRemarks + '\'' +
                ", timeDate='" + timeDate + '\'' +
                ", timeUserId=" + timeUserId +
                '}';
    }
}
