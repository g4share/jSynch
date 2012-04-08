package com.g4share.jSynch.guice.Binder;

import com.g4share.jSynch.guice.Factory.BinderHelperFactory;
import com.g4share.jSynch.guice.GuiceAbstractBinderHelper;
import com.g4share.jSynch.guice.GuiceDevelopmentBinderHelper;
import com.g4share.jSynch.guice.GuiceProductionBinderHelper;
import com.g4share.jSynch.guice.annotations.DevelopmentEnvironment;
import com.g4share.jSynch.guice.annotations.ProductionEnvironment;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * User: gm
 * Date: 4/7/12
 */
public class BinderHelperModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(GuiceAbstractBinderHelper.class, GuiceProductionBinderHelper.class)
                .build(Key.get(BinderHelperFactory.class, ProductionEnvironment.class)));

        install(new FactoryModuleBuilder()
                .implement(GuiceAbstractBinderHelper.class, GuiceDevelopmentBinderHelper.class)
                .build(Key.get(BinderHelperFactory.class, DevelopmentEnvironment.class)));

    }
}
