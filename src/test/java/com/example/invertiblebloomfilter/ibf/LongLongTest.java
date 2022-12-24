package com.example.invertiblebloomfilter.ibf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class LongLongTest {

    @Test
    void testLpad() {
        LongLong x = new LongLong("123");
        String result = x.lpadZero(x.getValue(), 10);
        Assert.assertEquals(10, result.length());
        Assert.assertEquals("0000000123", result);
    }

    @Test
    void testLpadShouldReturnTrue() {
        LongLong x = new LongLong("123");
        String result = x.lpadZero(x.getValue(), 3);
        Assert.assertEquals(3, result.length());
        Assert.assertEquals("123", result);
    }

    @Test
    void testLpadShouldReturnEquals() {
        LongLong x = new LongLong("123");
        String result = x.lpadZero(x.getValue(), 1);
        Assert.assertEquals(3, result.length());
        Assert.assertEquals("123", result);
    }

    @Test
    void testTrimLeftZero() {
        LongLong x = new LongLong("000123");
        x.trimLeftZero();
        Assert.assertEquals(3, x.getValue().length());
        Assert.assertEquals("123", x.getValue());

    }

    @Test
    void testTrimLeftZero2() {
        LongLong x = new LongLong("000000000");
        x.trimLeftZero();
        Assert.assertEquals("0", x.getValue());
    }


    @Test
    void testTrimLeftZero3() {
        LongLong x = new LongLong("");
        x.trimLeftZero();
        Assert.assertEquals("0", x.getValue());
    }

    @Test
    void testSubtract2Positive() {
        LongLong a = new LongLong("12345");
        LongLong b = new LongLong("11111");

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals("1234", c.getValue());
    }

    @Test
    void testSubtract2Positive1() {
        LongLong a = new LongLong("11111");
        LongLong b = new LongLong("12345");

        int aa = Integer.parseInt(a.getValue(), 16);
        int bb = Integer.parseInt(b.getValue(), 16);

        int cc = aa - bb;

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals(toSignedHexString(cc), c.getValue());
    }

    @Test
    void testSubtract2Positive2() {
        LongLong a = new LongLong("abc");
        LongLong b = new LongLong("def");

        int aa = Integer.parseInt(a.getValue(), 16);
        int bb = Integer.parseInt(b.getValue(), 16);

        int cc = aa - bb;

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals(toSignedHexString(cc), c.getValue());
    }


    @Test
    void testSubtract2Positive3() {
        LongLong a = new LongLong("ab");
        LongLong b = new LongLong("0");

        int aa = Integer.parseInt(a.getValue(), 16);
        int bb = Integer.parseInt(b.getValue(), 16);

        int cc = aa - bb;

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals(toSignedHexString(cc), c.getValue());
    }
    @Test
    void testSubtract2Positive4() {
        LongLong a = new LongLong("000");
        LongLong b = new LongLong("ab");

        int aa = Integer.parseInt(a.getValue(), 16);
        int bb = Integer.parseInt(b.getValue(), 16);

        int cc = aa - bb;

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals(toSignedHexString(cc), c.getValue());
    }


    @Test
    void testSubtract2Positive5() {
        LongLong a = new LongLong("");
        LongLong b = new LongLong("ab");

        int aa = Integer.parseInt(a.getValue(), 16);
        int bb = Integer.parseInt(b.getValue(), 16);

        int cc = aa - bb;

        LongLong c = LongLong.subtract2Positive(a, b);
        Assert.assertEquals(toSignedHexString(cc), c.getValue());
    }


    String toSignedHexString(int a) {
        if (a < 0) {
            return "-" + Integer.toHexString(-a);
        }
        return Integer.toHexString(a);
    }
}
