package com.lotlyz.lotday.model;

import java.io.Serializable;

public class TimeType implements Serializable {
    private Integer timetypeId;

    private String timetypeName;

    private Integer timetypeUserId;

    private static final long serialVersionUID = 1L;

    public Integer getTimetypeId() {
        return timetypeId;
    }

    public void setTimetypeId(Integer timetypeId) {
        this.timetypeId = timetypeId;
    }

    public String getTimetypeName() {
        return timetypeName;
    }

    public void setTimetypeName(String timetypeName) {
        this.timetypeName = timetypeName;
    }

    public Integer getTimetypeUserId() {
        return timetypeUserId;
    }

    public void setTimetypeUserId(Integer timetypeUserId) {
        this.timetypeUserId = timetypeUserId;
    }
}