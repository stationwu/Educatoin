package com.edu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringConverter{
    //private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String timeFormat = "HH:mm:ss";

    public static String convertDatetoString(Date date) {
    	DateFormat dateFormat = new SimpleDateFormat(shortDateFormat);
    	return dateFormat.format(date);
    }
    
    public static String convertTimetoString(Date date) {
    	DateFormat dateFormat = new SimpleDateFormat(timeFormat);
    	return dateFormat.format(date);
    }
}