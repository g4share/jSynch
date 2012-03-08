package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.log.LoggerProperties;

/**
 * Date: 3/8/12
 * Time: 12:38 AM
 */
public interface LoggerPropertiesFactory {
    LoggerProperties create(String logPath);
}
