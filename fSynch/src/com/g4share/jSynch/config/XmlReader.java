package com.g4share.jSynch.config;

import com.g4share.jSynch.share.service.Constants;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface XmlReader {
    void setStore(ConfigStore configStore);
    ConfigStore getStore();

    Constants.Codes read(String path);
}
