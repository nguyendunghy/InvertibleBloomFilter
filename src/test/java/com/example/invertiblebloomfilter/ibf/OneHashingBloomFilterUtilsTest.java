package com.example.invertiblebloomfilter.ibf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class OneHashingBloomFilterUtilsTest {

    @Test
    void testMod1() {
        long a = OneHashingBloomFilterUtils.mod("4E242F05C7C13074900E48ECE2389A83", 37, 16) + 0;
        Assert.assertEquals(4, a);


    }

    @Test
    void testMod2() {
        long b = OneHashingBloomFilterUtils.mod("4E242F05C7C13074900E48ECE2389A83", 41, 16) + 37;
        Assert.assertEquals(64, b);


    }

    @Test
    void testMod3() {

        long c = OneHashingBloomFilterUtils.mod("4E242F05C7C13074900E48ECE2389A83", 43, 16) + 78;
        Assert.assertEquals(83, c);

    }


    @Test
    void testMod4() {

        long c = OneHashingBloomFilterUtils.mod("103867660089343944542382758831857375875", 43, 10) + 78;
        Assert.assertEquals(83, c);

    }


    @Test
    void testMod() {
        List<String> listA = Arrays.asList("100", "100","12003420","2314598323","47","000000000","1");
        List<Long> listDivisors = Arrays.asList(2L, 12L,123L,47L,1000L, 121L,12342L);


        for (int i = 0; i < listA.size(); i++) {
            String a = listA.get(i);
            long mod = OneHashingBloomFilterUtils.mod(a, listDivisors.get(i), 10);
            Assert.assertEquals(Long.parseLong(listA.get(i)) % listDivisors.get(i), mod);
        }

    }
}
