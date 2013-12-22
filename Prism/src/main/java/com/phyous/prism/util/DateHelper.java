package com.phyous.prism.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    /*
    Given a date, format it in the way it should be displayed in a titlebar
    Inspired by:
    http://developer.android.com/reference/java/util/GregorianCalendar.html
     */
    public static String getDateTitle(Date d){
        return new SimpleDateFormat("EE, MMM d").format(d);
    }
}
