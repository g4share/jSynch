package com.g4share.jSynch.guice;

import com.g4share.jSynch.config.ConfigReader;
import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlReader;
import com.g4share.jSynch.guice.Binder.AbstractBindModule;
import com.g4share.jSynch.guice.Binder.ProductionBindModule;
import com.g4share.jSynch.guice.Factory.*;
import com.g4share.jSynch.guice.annotations.DefaultLogger;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.service.StatusInfo;
import com.g4share.jSynch.share.service.SynchManager;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;

/**
 * User: gm
 * Date: 3/11/12
 */
public class GuiceProductionBinderHelper extends GuiceAbstractBinderHelper {
    @Inject
    public GuiceProductionBinderHelper(@Assisted String currentPath) {
        super(currentPath);
    }

    @Override
    public AbstractBindModule getBindModuleInternal() {
        return new ProductionBindModule();
    }

    @Override
    public Logger getDefaultLoggerInternal(){
        return injector.getInstance(Key.get(Logger.class, DefaultLogger.class));
    }

    @Override
    public Logger getLoggerInternal(){
        LoggerPropertiesFactory loggerPropertiesFactory = injector.getInstance(LoggerPropertiesFactory.class);
        LoggerFactory loggerFactory = injector.getInstance(LoggerFactory.class);
        return loggerFactory.create(LogLevel.getDefaultLevel(), loggerPropertiesFactory.create(currentPath + "/log"));

    }

    @Override
    public SynchManager getSynchManagerInternal(){
        SynchManagerFactory synchManagerFactory = injector.getInstance(SynchManagerFactory.class);
        return synchManagerFactory.create(getLogger());
    }

    @Override
    protected ConfigStore getConfigStoreInternal() {
        ConfigStoreFactory configStoreFactory = injector.getInstance(ConfigStoreFactory.class);
        return configStoreFactory.create(getLogger());
    }

    @Override
    protected XmlReader getXmlReaderInternal() {
        XmlReaderFactory xmlReaderFactory = injector.getInstance(XmlReaderFactory.class) ;
        return xmlReaderFactory.create(getConfigStore());

    }

    @Override
    protected ConfigReader getConfigReaderInternal() {
        ConfigReaderFactory readerFactory = injector.getInstance(ConfigReaderFactory.class) ;
        return readerFactory.create(getLogger(), getXmlReader());
    }

    @Override
    protected StatusInfo getStatusInfoInternal() {
        StatusInfoFactory statusInfoFactory = injector.getInstance(StatusInfoFactory.class);
        return statusInfoFactory.create(getPointStoreHelperFactory());
    }

    @Override
    protected PointStoreHelperFactory getPointStoreHelperFactoryInternal() {
        return injector.getInstance(PointStoreHelperFactory.class);
    }
}
