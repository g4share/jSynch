package com.g4share.jSynch.share;

public class ConfigHash {
    private int interval;
    private PointHash[] pointHash;

    public ConfigHash(int interval, PointHash[] pointHash) {
        this.interval = interval;
        this.pointHash = pointHash;
    }

    public int getInterval() {
        return interval;
    }

    public PointHash[] getPointHash() {
        return pointHash;
    }
}
