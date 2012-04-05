package com.g4share.jSynch.log;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * User: gm
 * Date: 3/3/12
 */
@Service("fileProperties")
public class FileLoggerProperties implements LoggerProperties {
    private String fileName;

    @Inject
    @Autowired
    public FileLoggerProperties(
            @Assisted
            @Value("#{paramsStorage.configFileName}") String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
