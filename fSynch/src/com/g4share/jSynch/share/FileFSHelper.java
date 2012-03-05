package com.g4share.jSynch.share;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * User: gm
 * Date: 3/5/12
 */
public class FileFSHelper implements PointStoreHelper{

    private String path;

    public FileFSHelper(String path) {
        this.path = path;
    }

    private File[] load(String relativePath) {
        String absolutePath = combine(relativePath);
        return new File(absolutePath).listFiles();
    }

    @Override
    public String[] getFolders(String relativePath) {
        File[] listOfFiles = load(relativePath);
        if (listOfFiles == null || listOfFiles.length == 0) return null;

        Set<String> folders = new HashSet<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                folders.add(listOfFiles[i].getName());
            }
        }

        return folders.isEmpty()
                ? null
                : folders.toArray(new String[folders.size()]);
    }

    @Override
    public String[] getFiles(String relativePath) {
        File[] listOfFiles = load(relativePath);
        if (listOfFiles == null || listOfFiles.length == 0) return null;

        Set<String> files = new HashSet<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files.add(listOfFiles[i].getName());
            }
        }

        return files.isEmpty()
                ? null
                : (String[]) files.toArray(new String[files.size()]);
    }

    @Override
    public boolean folderExists(String relativePath) {
        String absolutePath = combine( relativePath);
        File folder = new File(absolutePath);
        if (!folder.exists()) return false;
        return folder.isDirectory();
    }

    @Override
    public boolean fileExists(String relativePath) {
        String absolutePath = combine(relativePath);
        File file = new File(absolutePath);
        if (!file.exists()) return false;
        return file.isFile();
    }

    @Override
    public long getSize(String relativePath) {
        String absolutePath = combine(relativePath);
        return new File(absolutePath).length();
    }

    @Override
    public void folderCreate(String relativePath){
        String absolutePath = combine(relativePath);
        new File(absolutePath).mkdirs();
    }

    public String combine(String... paths){
        String localPath = path;
        for (int i = 0; i < paths.length; i++) {
            if (paths[i].equals("")
                    || paths[i].equals(Constants.JAVA_PATH_DELIMITER + "")) continue;
            if (localPath.charAt(localPath.length() - 1) != Constants.JAVA_PATH_DELIMITER
                    && paths[i].charAt(0) != Constants.JAVA_PATH_DELIMITER){
                localPath += Constants.JAVA_PATH_DELIMITER;
            }
            localPath += paths[i];
        }

        return localPath;
    }
}
