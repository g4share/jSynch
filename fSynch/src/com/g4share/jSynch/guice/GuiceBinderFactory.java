package com.g4share.jSynch.guice;

import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlReader;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.SynchManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * User: gm
 * Date: 3/11/12
 */
public class GuiceBinderFactory {
    Injector injector;

    Logger defaultLogger;
    Logger fileLogger;

    SynchManager synchManager;
    PointStoreHelperFactory pointStoreHelperFactory;

    LogLevel level = LogLevel.TRACE;
    
    String currentPath;
    
    public GuiceBinderFactory(String currentPath) {
        this.currentPath = currentPath;
        injector = Guice.createInjector(new BindModule());
    }
    
    public Logger getDefaultLogger(){
        if (defaultLogger == null) {
            defaultLogger = injector.getInstance(Key.get(Logger.class, DefaultLogger.class));
            defaultLogger.setLevel(level);
        }
        return defaultLogger;
    }
    
    public Logger getLogger(){
        if (fileLogger == null) {
            LoggerPropertiesFactory loggerPropertiesFactory = injector.getInstance(LoggerPropertiesFactory.class);
            LoggerFactory loggerFactory = injector.getInstance(LoggerFactory.class);
            fileLogger = loggerFactory.create(LogLevel.getDefaultLevel(), loggerPropertiesFactory.create(currentPath + "/log"));
            fileLogger.setLevel(level);
        }

        return fileLogger;
    }

    public SynchManager getSynchManager(){
        if (synchManager == null) {
            SynchManagerFactory synchManagerFactory = injector.getInstance(SynchManagerFactory.class);        
            synchManager = synchManagerFactory.create(fileLogger);
        }
        return synchManager;
    }

    public ConfigStore readConfigStore(){
        ConfigStoreFactory configStoreFactory = injector.getInstance(ConfigStoreFactory.class);
        ConfigStore configStore = configStoreFactory.create(fileLogger);

        XmlReaderFactory readerFactory = injector.getInstance(XmlReaderFactory.class) ;
        XmlReader xmlReader = readerFactory.create(configStore);
        Constants.Codes configReadCode = xmlReader.read(currentPath + "/config.xml");

        if (configReadCode != Constants.Codes.SUCCESS_CODE) return null;

        level = configStore.getLogLevel();
        if (defaultLogger == null) defaultLogger.setLevel(level);
        if (fileLogger == null) fileLogger.setLevel(level);

        return configStore;
    }

    public PointStoreHelperFactory getPointStoreHelperFactory(){
        if (pointStoreHelperFactory == null){
            pointStoreHelperFactory = injector.getInstance(PointStoreHelperFactory.class);
        }
        return pointStoreHelperFactory;
    }
}
