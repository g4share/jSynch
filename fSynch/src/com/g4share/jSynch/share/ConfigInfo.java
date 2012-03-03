package com.g4share.jSynch.share;

/**
 * User: gm
 * Date: 3/3/12
 */
public class ConfigInfo {
    private int interval;
    private PointInfo pointInfo[];

    public ConfigInfo(int interval, PointInfo[] pointInfo) {
        this.interval = interval;
        this.pointInfo = pointInfo;
    }

    /**
     *
     * @return the seconds interval to start data synchronizing
     */

    public int getInterval() {
        return interval;
    }

    /**
     *
     * @return array of points to be synchronized
     */
    public PointInfo[] getPointInfo() {
        return pointInfo;
    }
}
