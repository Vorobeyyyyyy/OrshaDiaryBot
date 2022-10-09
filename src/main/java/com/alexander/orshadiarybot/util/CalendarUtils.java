package com.alexander.orshadiarybot.util;

import java.util.Calendar;
import java.util.TimeZone;

public class CalendarUtils {

    public static Calendar createCalendar() {
        Calendar calendar = CalendarUtils.createCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC+3"));

        return calendar;
    }
}
