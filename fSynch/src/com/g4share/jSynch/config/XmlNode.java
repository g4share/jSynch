package com.g4share.jSynch.config;

import java.util.EnumSet;
import java.util.Iterator;

/**
 * User: gm
 * Date: 3/3/12
 */
public enum XmlNode {
    none(0),
    Root(1),
    Interval(Root.code),
    SynchronisedPaths(2),
    Path(Root.code + SynchronisedPaths.code);

    private int code;

    private XmlNode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isPathKey(EnumSet<XmlNode> path)
    {
        int value = code;
        Iterator<XmlNode> it = path.iterator();
        while(it.hasNext()){
            value -= it.next().code;
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
        return XmlNode.none;
    }
}
