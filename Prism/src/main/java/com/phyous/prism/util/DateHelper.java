package com.phyous.prism.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    /*
    Given a date, format it in the way it should be displayed in a titlebar
    Inspired by:
    http://developer.android.com/reference/java/util/GregorianCalendar.html
     */
    public static String getDateTitle(Date date) {
        return new SimpleDateFormat("EE, MMM d").format(date);
    }

    public static String getDateTitle(long dateMillis) {
        Date date = new Date(dateMillis);
        return new SimpleDateFormat("EE, MMM d").format(date);
    }

    /**
     * Get the long representation of the beginning of the current date
     * @return current date in system time
     */
    public static long getCurrentDateStartLong() {
        final Date currentDate = new Date(System.currentTimeMillis());
        final Date currentDateBeginning = new Date(
                currentDate.getYear(),
                currentDate.getMonth(),
                currentDate.getDay());

        return currentDateBeginning.getTime();
    }
}
