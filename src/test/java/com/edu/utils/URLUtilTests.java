package com.edu.utils;

import org.junit.Assert;
import org.junit.Test;

public class URLUtilTests {
    private static final String URL1 = "http://www.example.com/user/center";
    private static final String URL2 = "http://www.example.com/wx/srv/user/center";
    private static final String RELATIVE_PATH = "/user/center";

    @Test
    public void getHost() {
        String host = URLUtil.getHost(URL1);
        Assert.assertEquals("http://www.example.com", host);
    }

    @Test
    public void getServiceURLBeforePath() {
        String host = URLUtil.getServiceURLBeforePath(URL2, RELATIVE_PATH);
        Assert.assertEquals("http://www.example.com/wx/srv", host);
    }

    @Test
    public void getServiceURL() {
        String host = URLUtil.getServiceURLBeforePath(URL1, RELATIVE_PATH);
        Assert.assertEquals("http://www.example.com", host);
    }
}
