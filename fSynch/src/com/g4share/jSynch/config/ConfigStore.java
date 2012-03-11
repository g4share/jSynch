package com.g4share.jSynch.config;

import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.share.PointInfo;

import java.util.Map;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface ConfigStore {
    void AddNode(XmlNode node, Map<String, String> attributes);
    void EventOccurred(LogLevel level, String hint);

    int getInterval();
    PointInfo[] getPoints();
    LogLevel getLogLevel();
}