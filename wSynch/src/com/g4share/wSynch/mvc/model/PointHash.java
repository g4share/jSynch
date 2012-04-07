package com.g4share.wSynch.mvc.model;

import com.g4share.jSynch.share.PointStatus;

public class PointHash {
    private String name;
    private PathHash[] pathHash;

    private PointStatus status = PointStatus.getDefaultStatus();

    public PointHash(String name, PathHash[] pathHash) {
        this.name = name;
        this.pathHash = pathHash;
    }

    public String getName() {
        return name;
    }

    public PointStatus getStatus() {
        return status;
    }

    public void setStatus(PointStatus status) {
        this.status = status;
    }

    public PathHash[] getPathHash() {
        return pathHash;
    }
}
