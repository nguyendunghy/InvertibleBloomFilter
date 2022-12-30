package com.example.invertiblebloomfilter.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    void testPaddingLeftZero(){
        Assert.assertEquals("0000000abc", StringUtils.paddingLeftZero("abc", 10));

        Assert.assertEquals("abc", StringUtils.paddingLeftZero("abc", 1));

        Assert.assertEquals("0000000000", StringUtils.paddingLeftZero("", 10));

    }
}
