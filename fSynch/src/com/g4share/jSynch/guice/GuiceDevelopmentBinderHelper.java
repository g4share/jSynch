package com.g4share.jSynch.guice;

import com.g4share.jSynch.guice.Binder.AbstractBindModule;
import com.g4share.jSynch.guice.Binder.DevelopmentBindModule;
import com.g4share.jSynch.log.Logger;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * User: gm
 * Date: 3/11/12
 */
public class GuiceDevelopmentBinderHelper extends GuiceProductionBinderHelper {

    @Inject
    public GuiceDevelopmentBinderHelper(@Assisted String currentPath) {
        super(currentPath);
    }

    @Override
    public AbstractBindModule getBindModuleInternal() {
        return new DevelopmentBindModule();
    }

    @Override
    public Logger getLoggerInternal() {
        return super.getDefaultLogger();
    }
}
