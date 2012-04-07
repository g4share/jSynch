package com.g4share.jSynch.share;

public enum PointStatus {
    UNKNOWN (0, false, "Unknown"),
    WORKING(1, true, "Synchronising..."),
    IDLE(2, false, "Idle"),
    ERROR (3, false, "Finished because an error"),
    CONFLICT(4, false, "Finished with conflict");

    private int code;
    private boolean isWorking;
    private String description;

    private PointStatus(int code, boolean isWorking, String description) {
        this.code = code;
        this.isWorking= isWorking;
        this.description = description;
    }

    static public PointStatus getDefaultStatus() {
        return PointStatus.UNKNOWN;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
