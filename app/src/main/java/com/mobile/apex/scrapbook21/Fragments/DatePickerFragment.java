package com.mobile.apex.scrapbook21.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.mobile.apex.scrapbook21.model.Holiday;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener{

    private Holiday holiday;
    private boolean isStart;

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    public void setDateButton(Button dateButton) {
        this.dateButton = dateButton;
    }

    private Button dateButton;
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

    public void setStart(boolean startDate)
    {
        isStart = startDate;
    }

    // set boolean method

    public void onDateSet(DatePicker view, int day, int month, int year)
    {
        /**if boolean is true we set start date
        holiday.setDateButton(day, month, year);
        // else set end date*/
        if(isStart)
        {
            holiday.setStartDate(day, month, year);
            dateButton.setText(holiday.formatDate(true));
        }
        else
        {
            holiday.setEndDate(day, month, year);
            dateButton.setText(holiday.formatDate(false));

        }
    }

}
