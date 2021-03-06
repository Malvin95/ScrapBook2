package com.mobile.apex.scrapbook21.model;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hardingm on 02/02/18.
 */

public class HolidayData
{
    private Context context;
    private Holiday currentHoliday;
    private static HolidayData instance;
    public final List<Holiday> holidays;

    private HolidayData(Context context)
    {
        this.context = context;
        holidays = new ArrayList<Holiday>();
        //setCurrentHoliday(new Holiday(context));
    }

    public static HolidayData getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new HolidayData(context);
        }
        return instance;
    }

    /**
     * In case we don't have a context...
     * @return the singleton instance
     */
    private void addHoliday(Holiday holiday) {
        holidays.add(holiday);
    }

    public static HolidayData getInstance()
    {
        return instance;
    }

    public Holiday getCurrentHoliday()
    {
        return currentHoliday;
    }

    public List<Holiday> getAllHolidays()
    {
        return holidays;
    }

    public void setCurrentHoliday(Holiday currentHoliday)
    {
        this.currentHoliday = currentHoliday;
    }
}
