package com.g4share.jSynch.log;

/**
 * User: gm
 * Date: 3/3/12
 */
public class ConsoleLogger implements Logger {
    private LogLevel currentLevel;

    @Override
    public void logEvent(LogLevel level, String message) {
        if (currentLevel == null || level == null) return;
        if (level.isHighest(currentLevel)){
            System.out.println(level.getDescription() + " " + message);
        }
    }

    @Override
    public void setLevel(LogLevel level) {
        this.currentLevel = level;
    }
}
