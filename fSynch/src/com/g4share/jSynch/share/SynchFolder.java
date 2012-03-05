package com.g4share.jSynch.share;

/**
 * User: gm
 * Date: 3/5/12
 */
public class SynchFolder {
    private String relativePath;
    private SynchFolder[] folders;
    private SynchFile[] files;

    public SynchFolder(String relativePath, SynchFolder[] folders, SynchFile[] files) {
        this.relativePath = relativePath;
        this.folders = folders;
        this.files = files;
    }


    public String getRelativePath() {
        return relativePath;
    }

    public SynchFile[] getFiles() {
        return files;
    }

    public SynchFolder[] getFolders() {
        return folders;
    }
}
