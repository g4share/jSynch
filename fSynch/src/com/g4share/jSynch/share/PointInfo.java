package com.g4share.jSynch.share;

import java.util.HashSet;
import java.util.Set;

/**
 * User: gm
 * Date: 3/3/12
 */
public class PointInfo {
    private String name;
    private Set<String> storePaths;

    public PointInfo(String name) {
        this.name = name;
    }

    /**
     *
     * @return storePoint name
     */
    public String getName() {
        return name;
    }

    /**
     * add a mew path to this point
     * @param storePath path to be added
     */
    public void addStorePath(String storePath){
        if (storePaths == null) storePaths = new HashSet<>();
        storePaths.add(storePath);
    }

    /**
     *
     * @return paths to be synchronized
     */
    public String[] getStorePaths() {
        return  storePaths.toArray(new String[storePaths.size()]);
    }
}
