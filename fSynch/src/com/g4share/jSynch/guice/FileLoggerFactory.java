package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.Logger;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface FileLoggerFactory {
    Logger create(String logPath);
}
