package com.example.invertiblebloomfilter.utils;

import java.util.regex.Pattern;

public class StringUtils {
    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");


    public static boolean isNullOrEmpty(Object o){
        return o == null || "".equals(o.toString().trim());
    }

    public static boolean notNullOrEmpty(Object o){
        return !isNullOrEmpty(o);
    }


    public static boolean isNumeric(String strNum) {
        return pattern.matcher(strNum).matches();
    }
}
