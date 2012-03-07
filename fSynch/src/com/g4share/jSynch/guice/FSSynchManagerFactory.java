package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.SynchManager;

/**
 * User: gm
 * Date: 3/8/12
 */
public interface FSSynchManagerFactory {
    SynchManager create(Logger logger);
}
