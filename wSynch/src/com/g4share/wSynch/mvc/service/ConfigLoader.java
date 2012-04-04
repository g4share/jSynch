package com.g4share.wSynch.mvc.service;

import com.g4share.jSynch.config.*;
import com.g4share.jSynch.log.*;
import com.g4share.jSynch.share.ConfigInfo;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigLoader implements ConfigService {
    Logger logger;

    ConfigReader cReader;
    XmlReader xmlReader;
    ConfigStore cStore;

    private String configFileName;

    public ConfigLoader() {}
    public ConfigLoader(String loggerFileName, String configFileName) {
        logger = new FileLogger(new FileLoggerProperties(loggerFileName),
                new ConsoleLogger());

        cStore = new ConfigStorage(logger);

        xmlReader = new XmlFileReader();
        xmlReader.setStore(cStore);
        cReader = new XmlConfigReader(logger, xmlReader);
        this.configFileName = configFileName;
    }

    @Override
    public ConfigInfo getConfigInfo() {
        ConfigInfo cInfo = cReader.read(configFileName);
        return cInfo;
    }
}
