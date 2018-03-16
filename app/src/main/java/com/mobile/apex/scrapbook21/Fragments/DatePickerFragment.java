package com.mobile.apex.scrapbook21.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.mobile.apex.scrapbook21.R;
import com.mobile.apex.scrapbook21.model.Holiday;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener{
    private Holiday holiday;
    // boolean for setting the start date or end date
    // button to put the date in text format on the button

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //Use the current date as the default dat in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new instance of DatePickerDialogue and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void setHoliday(Holiday holiday, Button button) {
        this.holiday = holiday;
    }

    // set boolean method

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        //Do something with the date chosen by the user
        Calendar c  = Calendar.getInstance();
        c.set(year, month, day);
        // if boolean is true we set start date
        holiday.setStartDate(c.getTime());
        // else set end date
    }

}