package com.edu.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {
    private static final Pattern p = Pattern.compile("https?://\\w+\\.\\w+\\.\\w+");

    public static String getHostUrl(HttpServletRequest request) {
        return getHost(request.getRequestURL().toString());
    }

    public static String getHost(String url) {
        Matcher m = p.matcher(url);
        String host = "";
        if (m.find()) {
            host = m.group(0);
        }
        return host;
    }

    public static String getServiceURLBeforePath(String url, String subPath) {
        int index = url.indexOf(subPath);
        if (index != -1) {
            return url.substring(0, index);
        } else {
            return "";
        }
    }
}
