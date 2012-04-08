package com.g4share.jSynch.guice.Factory;

import com.g4share.jSynch.guice.GuiceAbstractBinderHelper;

/**
 * Date: 3/8/12
 * Time: 12:38 AM
 */
public interface BinderHelperFactory {
    GuiceAbstractBinderHelper create(String currentPath);
}
