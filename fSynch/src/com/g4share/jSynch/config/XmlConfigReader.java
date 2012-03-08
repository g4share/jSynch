package com.g4share.jSynch.config;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointInfo;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.*;

/**
 * User: gm
 * Date: 3/3/12
 */
public class XmlConfigReader implements ConfigReader {
    private Logger logger;
    private XmlReader xmlReader;

    @Inject
    public XmlConfigReader(@Assisted Logger logger,
                           XmlReader xmlReader){
        this.logger = logger;
        this.xmlReader = xmlReader;
    }

    @Override
    public ConfigInfo read(String path){
        Constants.Codes result = xmlReader.read(path);
        if (result == null) return null;

        int interval = xmlReader.getStore().getInterval();
        PointInfo[] points = xmlReader.getStore().getPoints();

        switch (interval){
            case 0:
                if (logger != null) {
                    logger.logFatal("Please specify interval (seconds).");
                }
                return null;
            case Constants.WRONG_NUMBER:
                if (logger != null) {
                    logger.logFatal("Incorect interval value.");
                }
                return null;
        }

        if (points.length == 0) {
            if (logger != null) {
                logger.logFatal("There are no points to be synchronized.");
            }

            return null;
        }

        //remove wrong points
        Set<PointInfo> pointInfo = new HashSet<>();
        for(PointInfo info : points){
            if (info == null || info.getStorePaths().length < 2){
                if (logger != null) {
                    logger.logError("Point \"" + info.getName() +  "\" removed. It should be at least 2 points to be synchronized.");
                }
                continue;
            }
            pointInfo.add(info);
        }

        if (pointInfo.isEmpty()) {
            if (logger != null) {
                logger.logFatal("There are no points to be synchronized.");
            }
            return null;
        }


        interval *= 1000;

        ConfigInfo config = new ConfigInfo(interval,
                pointInfo.toArray(new PointInfo[pointInfo.size()]));

        points = null;
        return config;
    }
}
