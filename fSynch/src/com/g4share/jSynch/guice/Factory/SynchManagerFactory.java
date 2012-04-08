package com.g4share.jSynch.guice.Factory;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.SynchManager;

/**
 * User: gm
 * Date: 3/8/12
 */
public interface SynchManagerFactory {
    SynchManager create(Logger logger);
}
