package com.mobile.apex.scrapbook21.model;

import android.content.Context;

import com.mobile.apex.scrapbook21.R;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hardingm on 02/02/18.
 */

public class Holiday implements Serializable {
    private String title;
    private String notes;
    private Date startDate;
    private Date endDate;

    public Holiday(String title, String notes/**, Date startDate, Date endDate*/) {
        this.title = title;
        this.notes = notes;
        //this.startDate = startDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
