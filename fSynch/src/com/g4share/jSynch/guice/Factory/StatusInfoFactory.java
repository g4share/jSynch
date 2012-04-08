package com.g4share.jSynch.guice.Factory;

import com.g4share.jSynch.share.StatusInfoLoader;

/**
 * User: gm
 */
public interface StatusInfoFactory {
    StatusInfoLoader create(PointStoreHelperFactory pointStoreHelperFactory);
}
