package com.g4share.jSynch.share.service;

/**
 * User: gm
 * Date: 3/3/12
 */
public final class Constants {
    public static final char WIN_PATH_DELIMITER = '\\';
    public static final char JAVA_PATH_DELIMITER = '/';

    public static final String ROOT = JAVA_PATH_DELIMITER + "";

    public static final int WRONG_NUMBER = Integer.MIN_VALUE;

    public static final String SECONDS_ATTRIBUTE = "seconds";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String LOG_LEVEL_ATTRIBUTE = "level";


    public enum Codes {
        FATAL_ERROR_CODE(-1),
        ERROR_CODE(1),
        SUCCESS_CODE(0);

        private int code;
        private Codes(int code) {
            this.code = code;
        }

        public boolean isError(){
            return code == ERROR_CODE.code;
        }
    }
}