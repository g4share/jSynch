package com.g4share.jSynch.share;

import com.g4share.jSynch.config.XmlNode;

import java.util.HashMap;

/**
 * User: gm
 * Date: 3/3/12
 */
public interface StoreManager {
    void AddNode(XmlNode node, HashMap<String, String> attributes);
    void ErrorOccurred(String hint);
}
