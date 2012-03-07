package com.g4share.jSynch.share;

import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointStoreHelper;
import com.g4share.jSynch.share.SynchFolder;

import java.nio.channels.FileChannel;

/**
 * User: gm
 * Date: 3/6/12
 */
public interface SynchManager {
    SynchFolder getFolder(PointStoreHelper pointHelper);
    Constants.Codes setFolder(PointStoreHelper pointHelper, String relativePath);

    Constants.Codes checkFile(PointStoreHelper pointHelper, SynchFile synchFile);
    void setFile(PointStoreHelper pointHelper, SynchFile synchFile, FileChannel source);
}
