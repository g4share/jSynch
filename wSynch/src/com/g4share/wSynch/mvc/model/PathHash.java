package com.g4share.wSynch.mvc.model;

public class PathHash {
    private int folders;
    private int files;
    private long size;

    private String path;

    public PathHash(String path) {
        this.path = path;
    }

    public int getFolders() {
        return folders;
    }

    public int getFiles() {
        return files;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public void AddHash(int folders, int files, long size){
        this.folders += folders;
        this.files += files;
        this.size += size;
    }
}
