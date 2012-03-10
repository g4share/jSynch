package com.g4share.jSynch;

import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlReader;
import com.g4share.jSynch.guice.*;
import com.g4share.jSynch.log.FileLoggerProperties;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.log.LoggerProperties;
import com.g4share.jSynch.share.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * User: gm
 * Date: 3/8/12
 * Time: 12:20 AM
 */
public class Runner {
    public static void main(String[] args){
        String currentPath = getJarLocation();

        Injector injector = Guice.createInjector(new BindModule());
        Logger defaultLogger = injector.getInstance(Key.get(Logger.class, DefaultLogger.class));
        defaultLogger.logEvent(currentPath);

        if (currentPath == null){
            defaultLogger.logFatal("Could not get current path.");
            System.exit(1);
        }

        LoggerPropertiesFactory loggerPropertiesFactory = injector.getInstance(LoggerPropertiesFactory.class);
        LoggerFactory loggerFactory = injector.getInstance(LoggerFactory.class);
        Logger fileLogger = loggerFactory.create(loggerPropertiesFactory.create(currentPath + "/log"));

        SynchManagerFactory synchManagerFactory = injector.getInstance(SynchManagerFactory.class);
        SynchManager synchManager = synchManagerFactory.create(fileLogger);

        ConfigStoreFactory  configStoreFactory = injector.getInstance(ConfigStoreFactory.class);
        ConfigStore configStore = configStoreFactory.create(fileLogger);

        XmlReaderFactory readerFactory = injector.getInstance(XmlReaderFactory.class) ;
        XmlReader xmlReader = readerFactory.create(configStore);
        Constants.Codes configReadCode = xmlReader.read(currentPath + "/config.xml");
        if (configReadCode != Constants.Codes.SUCCESS_CODE){
            fileLogger.logFatal("Could not read configuration. Exit.");
            System.exit(1);
        }

        PointStoreHelperFactory pointStoreHelperFactory = injector.getInstance(PointStoreHelperFactory.class);
        Synch(pointStoreHelperFactory, synchManager, configStore.getPoints());
    }

    private static void Synch(PointStoreHelperFactory pointStoreHelperFactory,
                              SynchManager synchManager,
                              PointInfo[] configs) {

        for (PointInfo config : configs) {
            for (final String pathFrom : config.getStorePaths()){
                for (final String pathTo : config.getStorePaths()){
                    if (pathFrom.equals(pathTo)) continue; // do not synch the same folder in the point

                    PointStoreHelper pointStoreFrom = pointStoreHelperFactory.create(pathFrom);
                    PointStoreHelper pointStoreTo = pointStoreHelperFactory.create(pathTo);

                    SynchFolder folderFrom = synchManager.getFolder(pointStoreFrom);
                    setFolder(synchManager, pointStoreFrom, pointStoreTo, folderFrom);
                }
            }
        }
    }

    static private void setFolder(SynchManager synchManager,
                                  PointStoreHelper pointStoreFrom,
                                  PointStoreHelper pointStoreTo,
                                  SynchFolder synchFolder) {

        Constants.Codes result = synchManager.setFolder(pointStoreTo, synchFolder.getRelativePath());

        //if folder could not be created, no synchronization for this path
        if (result == Constants.Codes.FATAL_ERROR_CODE){
            return;
        }

        if (synchFolder.getFolders() != null
                && synchFolder.getFolders().length > 0){

            for(SynchFolder innerFolder : synchFolder.getFolders()) {
                setFolder(synchManager, pointStoreFrom, pointStoreTo, innerFolder);
            }
        }

        for(SynchFile innerFile : synchFolder.getFiles()) {
            switch(synchManager.checkFile(pointStoreTo, innerFile)){
                case FATAL_ERROR_CODE :
                    return;
                case ERROR_CODE :
                    FileChannel source = pointStoreFrom.getReadChannel(innerFile.getName());
                    synchManager.setFile(pointStoreTo, innerFile, source);
            }
        }
    }
    private static String getJarLocation() {
        final ProtectionDomain domain;
        final CodeSource source;
        final URL url;
        final URI uri;

        domain = Runner.class.getProtectionDomain();
        source = domain.getCodeSource();
        url  = source.getLocation();

        try {
            uri = url.toURI();
        } catch (URISyntaxException ex) {
            return null;
        }

        String jarPath = uri.getPath();
        return "/E:/gitStore/jSynch/config";


        //return jarPath.substring(0, jarPath.lastIndexOf(Constants.JAVA_PATH_DELIMITER));
    }
}
