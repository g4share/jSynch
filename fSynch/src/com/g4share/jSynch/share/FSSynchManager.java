package com.g4share.jSynch.share;

import com.g4share.jSynch.log.Logger;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.nio.channels.FileChannel;

/**
 * User: gm
 * Date: 3/6/12
 */
public class FSSynchManager implements SynchManager {
    private Logger logger;

    @Inject
    public FSSynchManager(@Assisted Logger logger) {
        this.logger = logger;
    }

    /**
     *
     * @return information about items located in the folder to be synchronized
     */
    @Override
    public SynchFolder getFolder(PointStoreHelper pointHelper) {
        return getFolder(pointHelper, Constants.JAVA_PATH_DELIMITER + "");
    }

    private static SynchFolder getFolder(PointStoreHelper pointHelper, String relativePath){
        //get inner folders lists
        String[] folders = pointHelper.getFolders(relativePath);
        SynchFolder[] synchFolders = null;

        //if there is any folder
        if (folders != null && folders.length > 0) {
            //crate an array for each of them
            synchFolders = new SynchFolder[folders.length];

            for(int i = 0; i < folders.length; i++) {
                //and load folder info
                synchFolders[i] = getFolder(pointHelper, pointHelper.combine(relativePath,  folders[i]));
            }
        }

        //get files list in the folder
        String[] files = pointHelper.getFiles(relativePath);
        SynchFile[] synchFiles = null;

        if (files != null && files.length > 0) {
            synchFiles = new SynchFile[files.length];
            for(int i = 0; i < files.length; i++) {
                //and load file info
                synchFiles[i] = new SynchFile(files[i],
                        pointHelper.getSize(files[i]));
            }
        }
        return new SynchFolder(relativePath, synchFolders, synchFiles);
    }


    @Override
    public Constants.Codes setFolder(PointStoreHelper pointHelper, String relativePath){
        if (!pointHelper.folderExists(Constants.ROOT)){
            logError("Could not find root folder. Please create.", false);
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        if (pointHelper.fileExists(relativePath)){
            logError("Could not create \""  + relativePath
                    + "\" folder because there is a file at this location.", false);

            return Constants.Codes.FATAL_ERROR_CODE;
        }


        if (pointHelper.folderExists(relativePath)){
            return Constants.Codes.SUCCESS_CODE;
        }

        pointHelper.folderCreate(relativePath);

        if (!pointHelper.folderExists(relativePath)){
            logError("Could not create \""  + relativePath + "\" folder.", false);
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        logEvent("Folder \"" + relativePath + "\" has been created.");
        return Constants.Codes.SUCCESS_CODE;
    }

    @Override
    public Constants.Codes checkFile(PointStoreHelper pointHelper, SynchFile synchFile){
        if (!pointHelper.folderExists(Constants.ROOT)){
            logError("Could not find root folder. Please create.", false);
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        if (!pointHelper.fileExists(synchFile.getName())){
            return Constants.Codes.ERROR_CODE;
        }

        if (pointHelper.folderExists(synchFile.getName())){
            logError("Could not create \""  + synchFile.getName()
                    + "\" file because there is a folder at this location.", false);

            return Constants.Codes.SUCCESS_CODE;
        }

        long localSize = pointHelper.getSize(synchFile.getName());
        if (localSize != synchFile.getSize()){
            logger.logFatal("conflict for \"" + synchFile.getSize()
                    + "\" file (" + synchFile.getSize() + " - > " + localSize + ").");
        }

        return Constants.Codes.SUCCESS_CODE;
    }


    public void setFile(PointStoreHelper pointHelper, SynchFile synchFile, FileChannel source) {
        if (checkFile(pointHelper, synchFile) != Constants.Codes.ERROR_CODE){
            return;
        }
        pointHelper.writeChannel(source, synchFile.getName());
    }


    private void logEvent(String message){
        if (logger == null) return;
        logger.logEvent(message);
    }

    private void logError(String exception, boolean isFatal){
        if (logger == null) return;
        if (isFatal) logger.logFatal(exception);
        logger.logError(exception);
    }
}
