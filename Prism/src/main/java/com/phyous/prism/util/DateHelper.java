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

    public static long getDateLong(Date d) {
        return d.getTime();
    }
}
