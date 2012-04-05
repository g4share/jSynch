package com.g4share.wSynch.mvc.service;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ParamsStorage {
    private String configFileName;
    private String loggerFileName;

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public String getLoggerFileName() {
        return loggerFileName;
    }

    public void setLoggerFileName(String loggerFileName) {
        this.loggerFileName = loggerFileName;
    }
}
