package com.g4share.jSynch.share;

/**
 * User: gm
 * Date: 3/5/12
 */
public interface PointStoreHelper {
    boolean fileExists(String relativePath);
    boolean folderExists(String relativePath);

    String[] getFiles(String relativePath);
    String[] getFolders(String relativePath);

    long getSize(String relativePath);

    void folderCreate(String relativePath);

    String combine(String... paths);
}
