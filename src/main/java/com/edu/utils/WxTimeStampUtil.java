package com.edu.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Component
public class WxTimeStampUtil {
    private static final String FORMAT = "yyyyMMddHHmmss";
    private static final DateFormat sdf = new SimpleDateFormat(FORMAT);
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FORMAT);

    public String getCurrentTimeStamp() {
        return toWxTimeStamp(LocalDateTime.now());
    }

    public String toWxTimeStamp(Date date) {
        return sdf.format(date);
    }

    public String toWxTimeStamp(Calendar calendar) {
        return sdf.format(calendar.getTime());
    }

    public String toWxTimeStamp(LocalDateTime localDateTime) {
        return dtf.format(localDateTime);
    }
}
