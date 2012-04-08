package com.g4share.jSynch.guice.Factory;

import com.g4share.jSynch.config.ConfigReader;
import com.g4share.jSynch.config.XmlReader;
import com.g4share.jSynch.log.Logger;

/**
 * User: gm
 * Date: 3/8/12
 */
public interface ConfigReaderFactory {
    ConfigReader create(Logger logger, XmlReader xmlReader);
}
