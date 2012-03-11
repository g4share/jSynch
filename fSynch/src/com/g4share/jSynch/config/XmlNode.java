package com.g4share.jSynch.config;

import java.util.EnumSet;

/**
 * User: gm
 * Date: 3/3/12
 */
public enum XmlNode {
    NONE(0),
    ROOT(1),
    INTERVAL(ROOT.code),
    LOG(ROOT.code),
    SYNCHRONISEDPATHS(2),
    PATH(ROOT.code + SYNCHRONISEDPATHS.code);

    private int code;

    private XmlNode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isPathKey(EnumSet<XmlNode> path) {
        int value = code;
        for (XmlNode aPath : path) {
            value -= aPath.code;
        }

        return value == 0;
    }

    public static XmlNode fromString(String text) {
        if (text != null) {
            for (XmlNode node : XmlNode.values()) {
                if (node.name().equalsIgnoreCase(text)){
                    return node;
                }
            }
        }
        return XmlNode.NONE;
    }
}
