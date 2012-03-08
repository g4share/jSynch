package com.g4share.jSynch.guice;

import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlReader;

/**
 * User: gm
 * Date: 3/8/12
 */
public interface XmlReaderFactory {
    XmlReader create(ConfigStore configStore);
}
