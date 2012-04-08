package com.g4share.wSynch.mvc.service;

import com.g4share.jSynch.config.ConfigReader;
import com.g4share.jSynch.share.ConfigHash;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.service.StatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("W3StatusInfoLoaderService")
public class W3StatusInfoLoader implements W3StatusInfo {
    ParamsStorage storage;
    ConfigReader cReader;
    StatusInfo statusInfo;

    @Autowired
    public W3StatusInfoLoader(@Qualifier("paramsStorage") ParamsStorage storage,
                              @Qualifier("xmlConfigReader") ConfigReader configReader,
                              @Qualifier("statusInfoReader") StatusInfo statusInfo) {

        this.storage = storage;
        this.statusInfo = statusInfo;
        cReader = configReader;
    }

    @Override
    public ConfigHash getConfigHash() {
        ConfigInfo cInfo = cReader.read(storage.getConfigFileName());
        return statusInfo.getConfigHash(cInfo);
    }
}
