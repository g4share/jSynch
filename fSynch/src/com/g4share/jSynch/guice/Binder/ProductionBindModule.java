package com.g4share.jSynch.guice.Binder;

import com.g4share.jSynch.guice.Factory.LoggerFactory;
import com.g4share.jSynch.guice.Factory.LoggerPropertiesFactory;
import com.g4share.jSynch.guice.annotations.DefaultLogger;
import com.g4share.jSynch.log.*;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * User: gm
 * Date: 3/3/12
 */
public class ProductionBindModule extends AbstractBindModule {
    @Override
    protected void configure() {
        bind(Logger.class)
                .annotatedWith(DefaultLogger.class)
                .to(ConsoleLogger.class)
                .in(Scopes.SINGLETON);

        install(new FactoryModuleBuilder()
                .implement(LoggerProperties.class, FileLoggerProperties.class)
                .build(LoggerPropertiesFactory.class));

        install(new FactoryModuleBuilder()
                .implement(Logger.class, FileLogger.class)
                .build(LoggerFactory.class));

        super.configure();
    }
}

