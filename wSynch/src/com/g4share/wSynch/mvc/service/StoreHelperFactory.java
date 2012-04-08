package com.g4share.wSynch.mvc.service;

import com.g4share.jSynch.guice.Factory.PointStoreHelperFactory;
import com.g4share.jSynch.share.FileFSHelper;
import com.g4share.jSynch.share.service.PointStoreHelper;
import org.springframework.stereotype.Service;

/**
 * User: gm
 * Date: 4/8/12
 */
@Service("PointStoreHelperFactory")
//todo DI
public class StoreHelperFactory implements PointStoreHelperFactory {

    @Override
    public PointStoreHelper create(String path) {
        return new FileFSHelper(path);
    }
}
