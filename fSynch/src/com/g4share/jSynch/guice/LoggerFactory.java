package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.*;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface LoggerFactory {
    Logger create(LogLevel currentLevel, LoggerProperties fileProperties);
}
