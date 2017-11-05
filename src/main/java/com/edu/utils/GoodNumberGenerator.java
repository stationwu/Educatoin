package com.edu.utils;

public class GoodNumberGenerator {
    private static final char _4_ = '4';
    public long next(long current, int step) {
        long nextNumber = current + (long)step;
        String nextNumberStr = String.valueOf(nextNumber);

        // Avoid '4'
        int i = nextNumberStr.indexOf(_4_);
        int l = nextNumberStr.length();
        for (; i != -1; i = nextNumberStr.indexOf(_4_)) {
            int indexFromRight = l - i;
            int power = indexFromRight - 1;
            nextNumber = nextNumber + (long)(Math.pow((double)10, (double)power)); // Ignore the step in such case
            nextNumberStr = String.valueOf(nextNumber);
        }

        return nextNumber;
    }
}
