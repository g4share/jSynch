package com.g4share.jSynch.log;

import com.g4share.jSynch.guice.DefaultLogger;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: gm
 * Date: 3/3/12
 */
@Service("fileLogger")
public class FileLogger implements Logger {
    private LogLevel currentLevel;

    private LoggerProperties fileProperties;
    private Logger innerLogger;

    @Inject
    @Autowired
    public FileLogger( @Qualifier("fileProperties") @Assisted LoggerProperties fileProperties,
                       @Qualifier("defaultLogger") @DefaultLogger Logger innerLogger) {
        this.fileProperties = fileProperties;
        this.innerLogger = innerLogger;

        if (innerLogger != null) innerLogger.logEvent(LogLevel.TRACE, this.fileProperties.getFileName());
    }

    @Override
    public void setLevel(LogLevel level) {
        this.currentLevel = level;
    }

    @Override
    public void logEvent(LogLevel level, String message) {
        if (currentLevel == null || level == null) return;

        if (level.isHighest(currentLevel)){
            printMessage(level.getDescription()
                    + " "
                    + new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS ").format(new Date())
                    + " "
                    + message);
        }
    }

    private void printMessage(String message){
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileProperties.getFileName(), true))) {
            writer.println(message);

        } catch (IOException e){
            if (innerLogger != null) innerLogger.logEvent(LogLevel.FATAL, "error logging message.");
        }
    }
}