package com.g4share.jSynch.log;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * User: gm
 * Date: 3/3/12
 */
public class FileLoggerProperties implements LoggerProperties {
    private String fileName;

    @Inject
    public FileLoggerProperties(@Assisted String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
