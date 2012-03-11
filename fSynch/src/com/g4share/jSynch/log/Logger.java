package com.g4share.jSynch.log;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface Logger {
    void logEvent(LogLevel level, String message);
    void setLevel(LogLevel level);
}
