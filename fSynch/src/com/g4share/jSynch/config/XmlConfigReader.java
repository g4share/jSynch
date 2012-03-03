package com.g4share.jSynch.config;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointInfo;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private Set<PointInfo> points;
    private int interval;


    @Override
    public ConfigInfo read(String path){
        points = new HashSet<>();
        //xmlReader.setManager(new ConfigManager());

        Constants.Codes result = xmlReader.read(path);
        if (result == null) return null;

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

        if (points.isEmpty()) {
            if (logger != null) {
                logger.logFatal("There are no points to be syncronized.");
            }

            return null;
        }

        //remove wrong points
        for (Iterator<PointInfo> iter = points.iterator(); iter.hasNext(); ) {
            PointInfo info = iter.next();
            if (info == null || info.getStorePaths().length < 2){
                if (logger != null) {
                    logger.logError("Point \"" + info.getName() +  "\" removed. It should be at least 2 points to be syncronized.");
                }
                iter.remove();
            }
        }

        if (points.isEmpty()) {
            if (logger != null) {
                logger.logFatal("There are no points to be syncronized.");
            }
            return null;
        }


        interval *= 1000;

        ConfigInfo config = new ConfigInfo(interval,
                points.toArray(new PointInfo[points.size()]));

        points = null;
        return config;
    }

}
