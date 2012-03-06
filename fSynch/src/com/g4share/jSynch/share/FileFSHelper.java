package com.g4share.jSynch.share;

import java.io.*;
import java.nio.channels.FileChannel;
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
        String absolutePath = combineInternal(false, relativePath);
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
                : files.toArray(new String[files.size()]);
    }

    @Override
    public boolean folderExists(String relativePath) {
        String absolutePath = combineInternal(false, relativePath);
        File folder = new File(absolutePath);
        if (!folder.exists()) return false;
        return folder.isDirectory();
    }

    @Override
    public boolean fileExists(String relativePath) {
        String absolutePath = combineInternal(false, relativePath);
        File file = new File(absolutePath);
        if (!file.exists()) return false;
        return file.isFile();
    }

    @Override
    public long getSize(String relativePath) {
        String absolutePath = combineInternal(false, relativePath);
        return new File(absolutePath).length();
    }

    @Override
    public void folderCreate(String relativePath){
        String absolutePath = combineInternal(false, relativePath);
        new File(absolutePath).mkdirs();
    }

    @Override
    public String combine(String... paths){
        return combineInternal(true, paths);
    }

    public String combineInternal(boolean relative, String... paths){
        if (paths.length == 0 && relative) return null;
        String localPath = (relative ? paths[0]: path).replace(Constants.WIN_PATH_DELIMITER, Constants.JAVA_PATH_DELIMITER);
        if (localPath.equals(""))  localPath = Constants.JAVA_PATH_DELIMITER + "";

        for (int i = relative ? 1 : 0; i < paths.length; i++) {
            if (paths[i].equals("")
                    || paths[i].equals(Constants.JAVA_PATH_DELIMITER + "")) continue;

            String processedPath = paths[i].replace(Constants.WIN_PATH_DELIMITER, Constants.JAVA_PATH_DELIMITER);;

            //delimiter should be added between
            if (localPath.charAt(localPath.length() - 1) != Constants.JAVA_PATH_DELIMITER
                    && processedPath.charAt(0) != Constants.JAVA_PATH_DELIMITER){
                localPath += Constants.JAVA_PATH_DELIMITER;
            }

            // fld1/ & /fld2
            if (localPath.charAt(localPath.length() - 1) == Constants.JAVA_PATH_DELIMITER
                && processedPath.charAt(0) == Constants.JAVA_PATH_DELIMITER){
                processedPath = processedPath.substring(1);
            }

            localPath += processedPath;
        }

        return localPath;
    }

    @Override
    public FileChannel getReadChannel(String pathRead) {
        String fileFrom = combine(path, pathRead);

        try {
            return new FileInputStream(fileFrom).getChannel();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean writeChannel(FileChannel source, String writePath) {
        String fileTo = combine(path, writePath);
        try (FileChannel destination = new FileOutputStream(fileTo).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
