package com.edu.utils;

import org.springframework.stereotype.Component;

@Component
public class WxPayConversionUtil {
    public String toOutTradeNo(long orderId) {
        // Weixin API docs says: 要求32个字符内，只能是数字、大小写字母_-|*@
        // But in Java the max number of type LONG is 2^63 - 1 which is no longer than 19 digits
        return String.format("%020d", orderId);
    }
}
