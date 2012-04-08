package com.g4share.jSynch.guice;

import com.g4share.jSynch.guice.Binder.BinderHelperModule;
import com.g4share.jSynch.guice.Factory.BinderHelperFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * User: gm
 * Date: 4/7/12
 */
public class GuiceBinderFactoryHelper {
    public BinderEnvironment environment;

    private Injector injector;
    private GuiceAbstractBinderHelper environmentBinderHelper;

    public GuiceBinderFactoryHelper(BinderEnvironment environment) {
        this.environment = environment;
        injector = Guice.createInjector(new BinderHelperModule());
    }


    public  GuiceAbstractBinderHelper getBinderHelper(String currentPath){
        if (environmentBinderHelper == null) {
            BinderHelperFactory binderHelperFactory = injector.getInstance(Key.get(BinderHelperFactory.class, environment.getAnnotation()));
            environmentBinderHelper =  binderHelperFactory.create(currentPath);
        }
        return environmentBinderHelper;
    }
}
