package com.g4share.wSynch.mvc.service;

import com.g4share.jSynch.config.ConfigReader;
import com.g4share.jSynch.share.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ConfigService")
public class ConfigLoader implements ConfigService {
    ParamsStorage storage;
    ConfigReader cReader;

    @Autowired
    public ConfigLoader(@Qualifier("paramsStorage") ParamsStorage storage,
                        @Qualifier("xmlConfigReader") ConfigReader configReader) {

        this.storage = storage;

        cReader = configReader;
    }

    @Override
    public ConfigInfo getConfigInfo() {
        ConfigInfo cInfo = cReader.read(storage.getConfigFileName());
        return cInfo;
    }
}
