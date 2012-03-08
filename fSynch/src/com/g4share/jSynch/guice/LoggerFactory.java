package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.log.LoggerProperties;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface LoggerFactory {
    Logger create(LoggerProperties fileProperties);
}
