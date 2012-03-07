package com.g4share.jSynch.log;

import com.g4share.jSynch.guice.DefaultLogger;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: gm
 * Date: 3/3/12
 */
public class FileLogger implements Logger {
    private LoggerProperties fileProperties;
    private Logger innerLogger;

    @Inject
    public FileLogger(@Assisted LoggerProperties fileProperties,
                      @DefaultLogger Logger innerLogger) {
        this.fileProperties = fileProperties;
        this.innerLogger = innerLogger;

        if (innerLogger != null) innerLogger.logEvent(this.fileProperties.getFileName());
    }


    @Override
    public void logEvent(String message) {
        printMessage("  ", message);
    }

    @Override
    public void logError(String exception) {
        printMessage("* ", exception);
    }

    @Override
    public void logFatal(String exception) {
        printMessage("! ", "Fatal error: " + exception);
    }

    private void printMessage(String errType, String message){
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileProperties.getFileName(), true))) {
            writer.print(errType);
            writer.print(new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS ").format(new Date()));
            writer.println(message);

        } catch (IOException e){
            if (innerLogger != null) innerLogger.logFatal("error loggind message.");
        }
    }
}