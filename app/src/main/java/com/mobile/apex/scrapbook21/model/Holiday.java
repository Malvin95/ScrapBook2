package com.mobile.apex.scrapbook21.model;

import android.content.Context;

import com.mobile.apex.scrapbook21.R;

import java.io.Serializable;

/**
 * Created by hardingm on 02/02/18.
 */

public class Holiday implements Serializable {
    private String title;
    private String notes;
    private String startDate;
    private String endDate;

    public Holiday(String title, String notes) {
        this.title = title;
        this.notes = notes;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
