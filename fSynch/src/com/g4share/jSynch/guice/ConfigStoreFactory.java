package com.g4share.jSynch.guice;

import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.log.Logger;

/**
 * User: gm
 * Date: 3/8/12
 */
public interface ConfigStoreFactory {
    ConfigStore create(Logger logger);
}
