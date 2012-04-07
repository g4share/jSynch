package com.g4share.wSynch.mvc.service;

import com.g4share.jSynch.config.ConfigReader;
import com.g4share.jSynch.share.*;
import com.g4share.wSynch.mvc.model.ConfigHash;
import com.g4share.wSynch.mvc.model.PathHash;
import com.g4share.wSynch.mvc.model.PointHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ConfigService")
public class ConfigLoader implements ConfigService {
    ParamsStorage storage;
    ConfigReader cReader;

    @Autowired
    public ConfigLoader(@Qualifier("paramsStorage") ParamsStorage storage,
                        @Qualifier("xmlConfigReader") ConfigReader configReader) {

        this.storage = storage;
        cReader = configReader;
    }

    @Override
    public ConfigHash getConfigHash() {
        ConfigInfo cInfo = cReader.read(storage.getConfigFileName());

        PointInfo[] pointInfo = cInfo.getPointInfo();
        PointHash[] pointHash = new PointHash[pointInfo.length];

        for (int i = 0; i < pointInfo.length; i++){
            String[] pointPath = pointInfo[i].getStorePaths();

            PathHash[] pathHash = new PathHash[pointPath.length];
            for (int j = 0; j < pointPath.length; j++){
                pathHash[j] = new PathHash(pointPath[j]);

                //todo: use spring DI for loading
                PointStoreHelper storeHelper = new FileFSHelper(pointPath[i]);

                setPointHash(pathHash[j], storeHelper, "/");
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
