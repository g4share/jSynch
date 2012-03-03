package com.g4share.jSynch.config;

import com.g4share.jSynch.share.ConfigInfo;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface ConfigReader {
    ConfigInfo read(String path);
}

