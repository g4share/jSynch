package com.g4share.jSynch.share;

import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointStoreHelper;
import com.g4share.jSynch.share.SynchFolder;

/**
 * User: gm
 * Date: 3/6/12
 */
public interface SynchManager {
    SynchFolder getFolder(PointStoreHelper pointHelper);
    Constants.Codes setFolder(SynchFolder folder, PointStoreHelper pointHelper);
}
