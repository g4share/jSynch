package com.g4share.jSynch.config;

import java.util.HashMap;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface ConfigStore {
    void AddNode(XmlNode node, HashMap<String, String> attributes);

    void ErrorOccured(String hint);
}