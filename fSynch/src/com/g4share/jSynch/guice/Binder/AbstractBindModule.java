package com.g4share.jSynch.guice.Binder;

import com.g4share.jSynch.config.*;
import com.g4share.jSynch.guice.Factory.*;
import com.g4share.jSynch.share.FSSynchManager;
import com.g4share.jSynch.share.FileFSHelper;
import com.g4share.jSynch.share.StatusInfoLoader;
import com.g4share.jSynch.share.service.PointStoreHelper;
import com.g4share.jSynch.share.service.StatusInfo;
import com.g4share.jSynch.share.service.SynchManager;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * User: gm
 * Date: 4/7/12
 */
public class AbstractBindModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(SynchManager.class, FSSynchManager.class)
                .build(SynchManagerFactory.class));

        install(new FactoryModuleBuilder()
                .implement(ConfigStore.class, ConfigStorage.class)
                .build(ConfigStoreFactory.class));

        install(new FactoryModuleBuilder()
                .implement(XmlReader.class, XmlFileReader.class)
                .build(XmlReaderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(ConfigReader.class, XmlConfigReader.class)
                .build(ConfigReaderFactory.class));

        install(new FactoryModuleBuilder()
                .implement(PointStoreHelper.class, FileFSHelper.class)
                .build(PointStoreHelperFactory.class));

        install(new FactoryModuleBuilder()
                .implement(StatusInfo.class, StatusInfoLoader.class)
                .build(StatusInfoFactory.class));

    }
}
