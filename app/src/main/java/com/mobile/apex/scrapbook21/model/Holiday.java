package com.mobile.apex.scrapbook21.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hardingm on 02/02/18.
 */

public class Holiday implements Serializable {
    private String title;
    private String notes;
    private SimpleDateFormat startDate;
    private Date endDate;

    public Holiday(String title, String notes, SimpleDateFormat startDate /**, Date endDate*/) {
        this.title = title;
        this.notes = notes;
        this.startDate = startDate;
        //this.endDate = endDate;
        //startDate = context.getString(R.string.start_date);
        //endDate = context.getString(R.string.end_date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public SimpleDateFormat getStartDate() {
        return startDate;
    }

    public void setStartDate(SimpleDateFormat startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date time) {
    }
}
