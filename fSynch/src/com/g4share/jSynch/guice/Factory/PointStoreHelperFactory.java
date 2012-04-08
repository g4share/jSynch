package com.g4share.jSynch.guice.Factory;

import com.g4share.jSynch.share.service.PointStoreHelper;

/**
 * User: gm
 * Date: 3/10/12
 */
public interface PointStoreHelperFactory {
    PointStoreHelper create(String path);
}
