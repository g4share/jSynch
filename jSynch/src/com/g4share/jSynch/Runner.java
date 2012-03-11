package com.g4share.jSynch;

import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.guice.GuiceBinderFactory;
import com.g4share.jSynch.guice.PointStoreHelperFactory;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: gm
 * Date: 3/8/12
 * Time: 12:20 AM
 */
public class Runner {
    public static void main(String[] args){
        String currentPath = getJarLocation();
        GuiceBinderFactory binderFactory = new GuiceBinderFactory(currentPath);
        Logger defaultLogger = binderFactory.getDefaultLogger();

        if (currentPath == null){
            defaultLogger.logEvent(LogLevel.TRACE, "Could not get current path.");
            System.exit(1);
        }

        final Logger fileLogger = binderFactory.getLogger();

        final SynchManager synchManager = binderFactory.getSynchManager();

        final ConfigStore configStore = binderFactory.readConfigStore();
        if (configStore == null){
            fileLogger.logEvent(LogLevel.TRACE, "Could not read configuration. Exit.");
            System.exit(1);
        }

        final PointStoreHelperFactory pointStoreHelperFactory = binderFactory.getPointStoreHelperFactory();

        new Timer(false).schedule(new TimerTask() {
            @Override
            public void run() {
                Synch(pointStoreHelperFactory,
                        synchManager,
                        configStore.getPoints(),
                        fileLogger);
            }}, 0, configStore.getInterval());

    }

    private static void Synch(PointStoreHelperFactory pointStoreHelperFactory,
                              SynchManager synchManager,
                              PointInfo[] configs,
                              Logger logger) {

        for (PointInfo config : configs) {
            logger.logEvent(LogLevel.INFO, config.getName());
            for (final String pathFrom : config.getStorePaths()){
                for (final String pathTo : config.getStorePaths()){
                    if (pathFrom.equals(pathTo)) continue; // do not synch the same folder in the point

                    logger.logEvent(LogLevel.INFO, "          " + pathFrom + " => " + pathTo);

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

        SynchFile[] innerFiles = synchFolder.getFiles();
        if (innerFiles == null) return;

        for(SynchFile innerFile : innerFiles) {
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
        //return "/E:/gitStore/jSynch/config";

        return jarPath.substring(0, jarPath.lastIndexOf(Constants.JAVA_PATH_DELIMITER));
    }
}
