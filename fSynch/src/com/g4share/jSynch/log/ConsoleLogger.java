package com.g4share.jSynch.log;

import org.springframework.stereotype.Service;

/**
 * User: gm
 * Date: 3/3/12
 */
@Service("defaultLogger")
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
