package com.g4share.jSynch;

import com.g4share.jSynch.guice.Factory.PointStoreHelperFactory;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.*;
import com.g4share.jSynch.share.service.Constants;
import com.g4share.jSynch.share.service.PointStoreHelper;
import com.g4share.jSynch.share.service.SynchManager;

import java.nio.channels.FileChannel;
import java.util.Timer;
import java.util.TimerTask;

/**
  * User: gm
 * Date: 4/8/12
 */
public class Worker {
    private PointStoreHelperFactory pointStoreHelperFactory;
    private SynchManager synchManager;
    private Logger logger;

    public Worker(PointStoreHelperFactory pointStoreHelperFactory,
                  SynchManager synchManager,
                  Logger logger) {
        this.pointStoreHelperFactory = pointStoreHelperFactory;
        this.synchManager = synchManager;
        this.logger = logger;
    }

    public void Synch(final ConfigInfo cInfo){
        new Timer(false).schedule(new TimerTask() {
            @Override
            public void run() {
                Synch(pointStoreHelperFactory,
                        synchManager,
                        cInfo.getPointInfo(),
                        logger);
            }}, 0, cInfo.getInterval());
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


    public void showStatus(ConfigHash cHash) {
        StringBuffer message = new StringBuffer();
        message.append("Interval: ").append(cHash.getInterval()).append("\n");

        for(PointHash pointHash : cHash.getPointHash()){
            message.append("Name: ").append(pointHash.getName()).append("  Status: ").append(pointHash.getStatus()).append("\n");
            for(PathHash pathHash : pointHash.getPathHash()){
                message.append("           ").append(pathHash.getPath()).append("  hash ")
                        .append(pathHash.getFolders()).append(":")
                        .append(pathHash.getFiles()).append(":")
                        .append(pathHash.getSize()).append(":").append("\n");
            }
        }

        logger.logEvent(LogLevel.CRITICAL, message.toString());
    }
}
