package com.g4share.jSynch.log;

/**
 * User: gm
 * Date: 3/3/12
 */
public class FileLoggerProperties implements LoggerProperties {
    private String fileName;

    public FileLoggerProperties(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
