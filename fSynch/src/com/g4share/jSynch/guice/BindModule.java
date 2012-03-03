package com.g4share.jSynch.guice;

import com.g4share.jSynch.log.ConsoleLogger;
import com.g4share.jSynch.log.FileLogger;
import com.g4share.jSynch.log.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * User: gm
 * Date: 3/3/12
 */
public class BindModule  extends AbstractModule {
    @Override
    protected void configure() {
        bind(Logger.class)
                .annotatedWith(DefaultLogger.class)
                .to(ConsoleLogger.class)
                .in(Scopes.SINGLETON);

        install(new FactoryModuleBuilder()
                .implement(Logger.class, FileLogger.class)
                .build(FileLoggerFactory.class));
    }
}

