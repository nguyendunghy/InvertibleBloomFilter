package com.example.invertiblebloomfilter.ibf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class LongLongTest {


    @Test
    void testSubtract1() {
        List<String> listA = Arrays.asList("", "A", "", "0", "B", "1234789", "adef", "23241", "-ffab", "-fa23", "-12", "0000000abc", "-000000000000000001abc");
        List<String> listB = Arrays.asList("", "", "B", "A", "0", "adef", "1234789", "-abc", "abc1234", "-a12", "-abcdef1", "-0000cdf", "-00012aff");

        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            LongLong b = new LongLong(listB.get(i));

            int aa = Integer.parseInt(a.getValue(), 16);
            int bb = Integer.parseInt(b.getValue(), 16);
            int cc = aa - bb;

            LongLong c = LongLong.subtract(a, b);
            Assert.assertEquals(toSignedHexString(cc).toUpperCase(), c.getValue());
        }

    }

    @Test
    void testSubtract2() {
        List<String> listA = Arrays.asList("-ffab");
        List<String> listB = Arrays.asList("abc1234");

        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            LongLong b = new LongLong(listB.get(i));

            int aa = Integer.parseInt(a.getValue(), 16);
            int bb = Integer.parseInt(b.getValue(), 16);
            int cc = aa - bb;

            LongLong c = LongLong.subtract(a, b);
            Assert.assertEquals(toSignedHexString(cc).toUpperCase(), c.getValue());
        }

    }

    @Test
    void testSum() {
        List<String> listA = Arrays.asList("", "A", "", "0", "B", "1234789", "adef", "23241", "-ffab", "-fa23", "-12", "0000000abc", "-000000000000000001abc");
        List<String> listB = Arrays.asList("", "", "B", "A", "0", "adef", "1234789", "-abc", "abc1234", "-a12", "-abcdef1", "-0000cdf", "-00012aff");

        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            LongLong b = new LongLong(listB.get(i));

            int aa = Integer.parseInt(a.getValue(), 16);
            int bb = Integer.parseInt(b.getValue(), 16);
            int cc = aa + bb;

            LongLong c = LongLong.sum(a, b);
            Assert.assertEquals(toSignedHexString(cc).toUpperCase(), c.getValue());
        }

    }

    @Test
    void testSum1() {
        List<String> listA = Arrays.asList("23241");
        List<String> listB = Arrays.asList("-abc");

        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            LongLong b = new LongLong(listB.get(i));

            int aa = Integer.parseInt(a.getValue(), 16);
            int bb = Integer.parseInt(b.getValue(), 16);
            int cc = aa + bb;

            LongLong c = LongLong.sum(a, b);
            Assert.assertEquals(toSignedHexString(cc).toUpperCase(), c.getValue());
        }

    }

    @Test
    void testMod() {
        List<String> listA = Arrays.asList("100", "100","12003420","2314598323","47","000000000","1", "-47","1000");
        List<Long> listDivisors = Arrays.asList(2L, 12L,123L,47L,1000L, 121L,12342L,1000L,-43L);


        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            long mod = a.mod(listDivisors.get(i));
            Assert.assertEquals(Long.parseLong(listA.get(i)) % listDivisors.get(i), mod);
        }

    }

    @Test
    void testMod1() {
        List<String> listA = Arrays.asList( "-47");
        List<Long> listDivisors = Arrays.asList( 1000L);
        System.out.println(-1001 %1000);


        for (int i = 0; i < listA.size(); i++) {
            LongLong a = new LongLong(listA.get(i));
            long mod = a.mod(listDivisors.get(i));
            Assert.assertEquals(Long.parseLong(listA.get(i)) % listDivisors.get(i), mod);
        }

    }

    String toSignedHexString(int a) {
        if (a < 0) {
            return "-" + Integer.toHexString(-a);
        }
        return Integer.toHexString(a);
    }
}
