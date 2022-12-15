package com.example.invertiblebloomfilter.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringUtils {
    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static final String SEPARATOR = "-";


    public static boolean isNullOrEmpty(Object o) {
        return o == null || "".equals(o.toString().trim());
    }

    public static boolean notNullOrEmpty(Object o) {
        return !isNullOrEmpty(o);
    }


    public static boolean isNumeric(String strNum) {
        return pattern.matcher(strNum).matches();
    }

    public static String join(long[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        String result = array[0] + "";
        for (int i = 1; i < array.length; i++) {
            result = result + SEPARATOR + array[i];
        }
        return result;
    }

    public static long[] split(String text) {
        if (org.apache.commons.lang.StringUtils.isEmpty(text)) {
            return new long[]{};
        }

        Long[] temp = Arrays.stream(text.split(SEPARATOR)).map(Long::valueOf).toArray(Long[]::new);
        return ArrayUtils.toPrimitive(temp);
    }
}
