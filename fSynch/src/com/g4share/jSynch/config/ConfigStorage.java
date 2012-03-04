package com.g4share.jSynch.config;

import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.PointInfo;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.*;

/**
 * User: gm
 * Date: 3/3/12
 */
public class ConfigStorage implements ConfigStore {
    private Set<PointInfo> points;
    private int interval;

    private Logger logger;

    @Inject
    public ConfigStorage(@Assisted Logger logger){
        this.logger = logger;
        this.points = new HashSet<>();
    }

    @Override
    public void ErrorOccured(String hint) {
        if (logger != null) {
            logger.logFatal(hint);
        }
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public Set<PointInfo> getPoints() {
        return points;
    }

    /**
     * store nodes as objects
     * @param node
     * @param attributes
     */
    @Override
    public void AddNode(XmlNode node, Map<String, String> attributes) {
        switch(node){
            case Interval:
                String intervalRaw = GetValue(attributes, Constants.SECONDS_ATTRIBUTE);
                interval = tryParse(intervalRaw);
                break;
            case Path:
                addNewPath(GetValue(attributes, Constants.NAME_ATTRIBUTE),
                        GetValue(attributes, Constants.VALUE_ATTRIBUTE));
        }
    }

    private int tryParse(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            ErrorOccured("Wrong interval: " + value);
            return Constants.WRONG_NUMBER;
        }
    }

    private String GetValue(Map<String, String> map, String key) {
        if (map == null) {
            ErrorOccured("Error: Empty keys map.");
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
        Iterator<PointInfo> it = points.iterator();
        while (it.hasNext()){
            PointInfo tempConfig = it.next();
            if (tempConfig.getName().equals(configName)){
                pInfo = tempConfig;
                break;
            }
        }

        //if such config not found, creates a new one and add to list
        if (pInfo == null){
            pInfo = new PointInfo(configName);
            points.add(pInfo);
        }

        //there is posibility in windows to set "\" base path
        path = path.replace(Constants.WIN_PATH_DELIMITER,
                Constants.JAVA_PATH_DELIMITER).trim();

        //remove last delimiter
        if (path.charAt(path.length() - 1) == Constants.JAVA_PATH_DELIMITER){
            path = path.substring(0, path.length() - 1);

        }

        pInfo.addStorePath(path);
    }
}