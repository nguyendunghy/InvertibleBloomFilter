package com.example.statrystesting.utils;

import java.util.HashMap;

public class Constant {

    public static final long INTERNAL_ERROR = 404L;
    public static Long SAVE_USER_REQUEST_NULL = 1000L;
    public static final Long SAVE_USER_REQUEST_NAME_NULL = 1001L;
    public static final Long SAVE_USER_REQUEST_PASSWORD_NULL = 1002L;

    public static final Long SAVE_USER_REQUEST_PHONE_INVALID = 1003L;
    public static final Long USER_NOT_EXIST = 1004L;
    public static final Long USER_ID_REQUIRED = 1005L;

    public static final Long SUCCESS_CODE = 200L;
    public static final String SUCCESS_MESS = "SUCCESS";


    public static HashMap<Long,String> MESSAGE_MAP = new HashMap<>();

    static {
        MESSAGE_MAP.put(SAVE_USER_REQUEST_NULL,"User data empty");
        MESSAGE_MAP.put(SAVE_USER_REQUEST_NAME_NULL,"User name empty");
        MESSAGE_MAP.put(SAVE_USER_REQUEST_PASSWORD_NULL,"User password empty");
        MESSAGE_MAP.put(SAVE_USER_REQUEST_PHONE_INVALID,"User phone number is invalid");
        MESSAGE_MAP.put(INTERNAL_ERROR,"Internal error !");
        MESSAGE_MAP.put(USER_NOT_EXIST,"User not exist");
        MESSAGE_MAP.put(USER_ID_REQUIRED,"User id required");

    }

}
