package com.g4share.jSynch.share;

import com.g4share.jSynch.log.Logger;

/**
 * User: gm
 * Date: 3/6/12
 */
public class FSSynchManager implements SynchManager {
    private String location;

    private Logger logger;

    public FSSynchManager(String location, Logger logger) {
        this.location = location;
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

    /**
     * synchronize folder with another one
     * @param folder folder to be synchronized
     * @return
     */
    @Override
    public Constants.Codes setFolder(SynchFolder folder, PointStoreHelper pointHelper){
        if (!pointHelper.folderExists(Constants.ROOT)){
            logError("Could not find root folder \""  + location + "\". Please create.", false);
            return Constants.Codes.FATAL_ERROR_CODE;
        }

        return setFolder(pointHelper, folder);
    }

    private Constants.Codes setFolder(PointStoreHelper pointHelper, SynchFolder folder) {
        Constants.Codes returnCode = Constants.Codes.SUCCESS_CODE;

        //get ffolder file list
        SynchFile[] files = folder.getFiles();

        //if there is any file
        if (files != null && files.length > 0){
            String relativePath = folder.getRelativePath();
            for(SynchFile file : files){
                //synchronize it
                SynchFile(pointHelper, file, relativePath);
            }
        }

        SynchFolder[] folders = folder.getFolders();
        if (folders == null || folders.length == 0) return returnCode;

        for(SynchFolder innerFolder : folders){
            String relativePath = innerFolder.getRelativePath();
            if (!pointHelper.folderExists(relativePath)){
                //synchronize it
                SynchFolder(pointHelper, relativePath);

                //do the same for each inner folder
                setFolder(pointHelper, innerFolder);
            }
        }
        return returnCode;
    }


    private void SynchFolder(PointStoreHelper pointHelper, String relativePath){
        if (pointHelper.fileExists(relativePath)){
            logError("Could not create \""  + relativePath
                    + "\" folder because there is a file at this location.", false);
            return;
        }
        pointHelper.folderCreate(relativePath);
        if (!pointHelper.folderExists(relativePath)){
            logError("Could not create \""  + relativePath + "\" folder.", false);
            return;
        }
        logEvent("Folder \"" + relativePath + "\" has been created.");
    }

    private void SynchFile(PointStoreHelper pointHelper, SynchFile file, String relativePath){
        String path = pointHelper.combine(relativePath, file.getName());

        //conflict are checked on FS copy (because here nobody
        //knows about full path)
        if (pointHelper.folderExists(path)){
            logError("Could not create \""  + path
                    + "\" file because there is a folder at this location.", false);
            return;
        }
        if (pointHelper == null) {
            logError("Could not synchtonise file \"" + path
                    + "\". No synch method found.", false);
            return;
        }

        //syncronise file
        boolean copiedNew = true;//pointHelper.Share(path);
        if (!pointHelper.fileExists(path)){
            logError("Could not synchronize \""  + path + "\" file.", false);
            return;
        }

        if (copiedNew) logEvent("File \"" + path + "\" has been syncronised.");
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
