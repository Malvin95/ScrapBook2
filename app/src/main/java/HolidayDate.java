import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hardingm on 23/03/18.
 */

public class HolidayDate implements Serializable {

    int day;
    int month;
    int year;

    public HolidayDate()
    {
        final Calendar calendar = Calendar.getInstance();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String formatDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.day, this.month, this.year);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public String toString()
    {
        return formatDate();
    }

}
