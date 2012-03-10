package com.g4share.jSynch.guice;

import com.g4share.jSynch.share.PointStoreHelper;

/**
 * User: gm
 * Date: 3/10/12
 */
public interface PointStoreHelperFactory {
    PointStoreHelper create(String path);
}
