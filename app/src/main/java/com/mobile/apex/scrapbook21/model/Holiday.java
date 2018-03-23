package com.mobile.apex.scrapbook21.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hardingm on 02/02/18.
 */

public class Holiday implements Serializable {
    private String title;
    private String notes;
    private Calendar startDate;
    private Calendar endDate;

    public Holiday(String title, String notes) {
        this.title = title;
        this.notes = notes;
        this.startDate = Calendar.getInstance();
        this.endDate = Calendar.getInstance();;
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

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(int day, int month, int year) {
        startDate.set(day, month, year);
    }

    public String formatStartDate()
    {
        int year = startDate.get(Calendar.YEAR);
        int month = startDate.get(Calendar.MONTH);
        int day = startDate.get(Calendar.DAY_OF_MONTH);

        return ""+day+"/"+month+"/"+year;

    }
}
