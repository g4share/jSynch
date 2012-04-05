package com.g4share.jSynch.config;

import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointInfo;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User: gm
 * Date: 3/3/12
 */

@Service("configStore")
public class ConfigStorage implements ConfigStore {
    private Set<PointInfo> points;
    private int interval;
    private LogLevel logLevel = LogLevel.getDefaultLevel();
    
    private Logger logger;

    @Inject
    @Autowired
    public ConfigStorage(@Qualifier("fileLogger") @Assisted Logger logger){
        this.logger = logger;
        this.points = new HashSet<>();
    }

    @Override
    public void EventOccurred(LogLevel level, String hint) {
        if (logger != null) {
            logger.logEvent(level, hint);
        }
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public PointInfo[] getPoints() {
        return points.toArray(new PointInfo[points.size()]);
    }

    @Override
    public LogLevel getLogLevel() {
        return logLevel;
    }

    @Override
    public void AddNode(XmlNode node, Map<String, String> attributes) {
        switch(node){
            case INTERVAL:
                String intervalRaw = GetValue(attributes, Constants.SECONDS_ATTRIBUTE);
                interval = tryParse(intervalRaw);
                break;
            case PATH:
                addNewPath(GetValue(attributes, Constants.NAME_ATTRIBUTE),
                        GetValue(attributes, Constants.VALUE_ATTRIBUTE));
                break;
            case LOG:
                String logLevelRaw = GetValue(attributes, Constants.LOG_LEVEL_ATTRIBUTE);
                LogLevel tempLevel = LogLevel.fromString(logLevelRaw);
                if (tempLevel != LogLevel.NONE) logLevel = tempLevel;
                break;
        }
    }

    private int tryParse(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            EventOccurred(LogLevel.FATAL, "Wrong interval: " + value);
            return Constants.WRONG_NUMBER;
        }
    }

    private String GetValue(Map<String, String> map, String key) {
        if (map == null) {
            EventOccurred(LogLevel.FATAL, "Error: Empty keys map.");
            return null;
        }

        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            if(mapEntry.getKey().equalsIgnoreCase(key)){
                return mapEntry.getValue();
            }
        }

        return null;
    }

    private void addNewPath(String configName, String path) {
        PointInfo pInfo = null;
        for (PointInfo tempConfig : points) {
            if (tempConfig.getName().equals(configName)) {
                pInfo = tempConfig;
                break;
            }
        }

        //if such config not found, creates a new one and add to list
        if (pInfo == null){
            pInfo = new PointInfo(configName);
            points.add(pInfo);
        }

        //there is possibility in windows to set "\" base path
        path = path.replace(Constants.WIN_PATH_DELIMITER,
                Constants.JAVA_PATH_DELIMITER).trim();

        //remove last delimiter
        if (path.charAt(path.length() - 1) == Constants.JAVA_PATH_DELIMITER){
            path = path.substring(0, path.length() - 1);

        }

        pInfo.addStorePath(path);
    }
}