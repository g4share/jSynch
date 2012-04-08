package com.g4share.jSynch.config;

import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointInfo;
import com.google.inject.assistedinject.Assisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

/**
 * User: gm
 * Date: 3/3/12
 */
@Service("xmlConfigReader")
public class XmlConfigReader implements ConfigReader {
    private Logger logger;
    private XmlReader xmlReader;

    @Inject
    @Autowired
    public XmlConfigReader(@Qualifier("fileLogger") @Assisted Logger logger,
                           @Qualifier("xmlReader") @Assisted XmlReader xmlReader){
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
                logEvent(LogLevel.FATAL, "Please specify interval (seconds).");
                return null;
            case Constants.WRONG_NUMBER:
                logEvent(LogLevel.FATAL, "Incorrect interval value.");
                return null;
        }

        if (points.length == 0) {
            logEvent(LogLevel.FATAL, "There are no points to be synchronized.");
            return null;
        }

        //remove wrong points
        Set<PointInfo> pointInfo = new HashSet<>();
        for(PointInfo info : points){
            if (info == null || info.getStorePaths().length < 2){
                logEvent(LogLevel.ERROR, "Point \"" + info.getName() +  "\" removed. It should be at least 2 points to be synchronized.");
                continue;
            }
            pointInfo.add(info);
        }

        if (pointInfo.isEmpty()) {
            logEvent(LogLevel.FATAL, "There are no points to be synchronized.");
            return null;
        }


        interval *= 1000;

        ConfigInfo config = new ConfigInfo(interval,
                pointInfo.toArray(new PointInfo[pointInfo.size()]));

        points = null;
        return config;
    }

    private void logEvent(LogLevel level, String message){
        if (logger == null) return;
        logger.logEvent(level, message);
    }
}
