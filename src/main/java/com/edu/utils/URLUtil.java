package com.edu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {
    private static final Pattern p = Pattern.compile("http://\\w+\\.\\w+\\.\\w+");

    public static String getHost(String url) {
        Matcher m = p.matcher(url);
        String host = "";
        if (m.find()) {
            host = m.group(0);
        }
        return host;
    }
}
