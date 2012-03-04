package com.g4share.jSynch.config;

import com.g4share.jSynch.share.PointInfo;

import java.util.Map;
import java.util.Set;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface ConfigStore {
    void AddNode(XmlNode node, Map<String, String> attributes);
    void ErrorOccured(String hint);

    int getInterval();
    Set<PointInfo> getPoints();
}