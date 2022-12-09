package com.example.lotday2.bean;

public class Timetype {
//    private int timetype_id;
//    private  String timetype_name;
//    private  int timetype_user_id;

    private int timetypeId;

    private String timetypeName;

    private int timetypeUserId;

    public Timetype() {
    }

    public Timetype(int timetypeId, String timetypeName, int timetypeUserId) {
        this.timetypeId = timetypeId;
        this.timetypeName = timetypeName;
        this.timetypeUserId = timetypeUserId;
    }

    public int getTimetypeId() {
        return timetypeId;
    }

    public void setTimetypeId(int timetypeId) {
        this.timetypeId = timetypeId;
    }

    public String getTimetypeName() {
        return timetypeName;
    }

    public void setTimetypeName(String timetypeName) {
        this.timetypeName = timetypeName;
    }

    public int getTimetypeUserId() {
        return timetypeUserId;
    }

    public void setTimetypeUserId(int timetypeUserId) {
        this.timetypeUserId = timetypeUserId;
    }

    @Override
    public String toString() {
        return "Timetype{" +
                "timetypeId=" + timetypeId +
                ", timetypeName='" + timetypeName + '\'' +
                ", timetypeUserId=" + timetypeUserId +
                '}';
    }
}
