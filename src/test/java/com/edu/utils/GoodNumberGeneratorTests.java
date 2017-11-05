package com.edu.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoodNumberGeneratorTests {
    private GoodNumberGenerator generator;

    @Before
    public void setup() {
        generator = new GoodNumberGenerator();
    }

    @Test
    public void nextOf_3_ShouldBe_5() {
        long next = generator.next(3, 1);
        Assert.assertEquals(5, next);
    }

    @Test
    public void nextOf_13_ShouldBe_15() {
        long next = generator.next(13, 1);
        Assert.assertEquals(15, next);
    }

    @Test
    public void nextOf_39_ShouldBe_50() {
        long next = generator.next(39, 1);
        Assert.assertEquals(50, next);
    }

    @Test
    public void nextOf_13999_ShouldBe_15000() {
        long next = generator.next(13999, 1);
        Assert.assertEquals(15000, next);
    }

    @Test
    public void nextOf_413_ShouldBe_515() {
        long next = generator.next(413, 1);
        Assert.assertEquals(515, next);
    }

    @Test
    public void nextOf_1439_ShouldBe_1550() {
        long next = generator.next(1439, 1);
        Assert.assertEquals(1550, next);
    }

    @Test
    public void nextOf_4456793_ShouldBe_5556795() {
        long next = generator.next(4456793, 1);
        Assert.assertEquals(5556795, next);
    }
}
