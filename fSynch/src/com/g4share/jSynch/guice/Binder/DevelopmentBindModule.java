package com.g4share.jSynch.guice.Binder;

import com.g4share.jSynch.guice.annotations.DefaultLogger;
import com.g4share.jSynch.log.ConsoleLogger;
import com.g4share.jSynch.log.Logger;
import com.google.inject.Scopes;

/**
 * User: gm
 * Date: 3/3/12
 */
public class DevelopmentBindModule extends AbstractBindModule {
    @Override
    protected void configure() {
        bind(Logger.class)
                .to(ConsoleLogger.class)
                .in(Scopes.SINGLETON);

        bind(Logger.class)
                .annotatedWith(DefaultLogger.class)
                .to(ConsoleLogger.class)
                .in(Scopes.SINGLETON);


        super.configure();
    }
}

