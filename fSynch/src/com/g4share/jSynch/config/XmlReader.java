package com.g4share.jSynch.config;

import com.g4share.jSynch.share.Constants;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface XmlReader {
    void setManager(ConfigStore storeManager);
    Constants.Codes read(String path);
}
