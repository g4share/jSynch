package com.g4share.jSynch.share;

import com.g4share.jSynch.guice.Factory.PointStoreHelperFactory;
import com.g4share.jSynch.share.service.Constants;
import com.g4share.jSynch.share.service.PointStoreHelper;
import com.g4share.jSynch.share.service.StatusInfo;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("statusInfoReader")
public class StatusInfoLoader implements StatusInfo {
    PointStoreHelperFactory pointStoreHelperFactory;

    @Inject
    @Autowired
    public StatusInfoLoader(@Qualifier("PointStoreHelperFactory") @Assisted PointStoreHelperFactory pointStoreHelperFactory) {
        this.pointStoreHelperFactory = pointStoreHelperFactory;
    }

    @Override
    public ConfigHash getConfigHash(ConfigInfo cInfo) {

        PointInfo[] pointInfo = cInfo.getPointInfo();
        PointHash[] pointHash = new PointHash[pointInfo.length];

        for (int i = 0; i < pointInfo.length; i++){
            String[] pointPath = pointInfo[i].getStorePaths();

            PathHash[] pathHash = new PathHash[pointPath.length];
            for (int j = 0; j < pointPath.length; j++){
                pathHash[j] = new PathHash(pointPath[j]);

                PointStoreHelper storeHelper = pointStoreHelperFactory.create(pointPath[i]);
                setPointHash(pathHash[j], storeHelper, Constants.JAVA_PATH_DELIMITER + "");
            }


            pointHash[i] = new PointHash(pointInfo[i].getName(), pathHash);
        }

        return new ConfigHash(cInfo.getInterval(), pointHash);

    }


    private static void setPointHash(PathHash pHash, PointStoreHelper storeHelper, String relativePath) {

        String[] folderList = storeHelper.getFolders(relativePath);
        int folders = 0;
        int files = 0;
        long size = 0;

        if (folderList != null){
            folders =+ folderList.length;

            for(String folderName : folderList){
                setPointHash(pHash, storeHelper, folderName);
            }

        }

        String[] fileList = storeHelper.getFiles(relativePath);
        if (fileList != null){
            files += fileList.length;

            for(String fileName : fileList){
                size += storeHelper.getSize(storeHelper.combine(relativePath, fileName));
            }
        }

        pHash.AddHash(folders, files, size);
    }
}
