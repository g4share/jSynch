package com.g4share.jSynch.log;

/**
 * User: gm
 * Date: 3/3/12
 */
public class ConsoleLogger implements Logger {
    @Override
    public void logEvent(String message) {
        System.out.println(message);
    }

    @Override
    public void logError(String exception) {
        System.out.println(exception);
    }

    @Override
    public void logFatal(String exception) {
        System.out.println("Fatal error: " + exception);
    }
}
