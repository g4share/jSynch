import com.g4share.jSynch.share.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.management.monitor.StringMonitor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
* User: gm
* Date: 3/6/12
*/
public class SynchManagerTest {
    SynchManager syncManager;


    @Test
    public void testPathCombine() throws Exception {

        syncManager = new FSSynchManager("/someFolder", null);

        SynchFolder folder = syncManager.getFolder(new MemoryStoryHelper());

        //assertThat(folder.getRelativePath(), is("/someFolder"));
        //assertThat(folder.getFiles().length, is(1));
        //assertThat(folder.getFiles()[0].getName(), is("/file"));

 //       assertThat(folder.getFolders().length, is(1));
  //      assertThat(folder.getFolders()[0].getRelativePath(), is("/fldA"));
  //      assertThat(folder.getFolders()[0].getFiles().length, is(2));

   //     String[] arr = new String[] {folder.getFolders()[0].getFiles()[0].getName(),
  //              folder.getFolders()[0].getFiles()[0].getName()};
  //      assertThat(arrayContainsItem(arr, "/fldA/fileA"), is(true));
  //      assertThat(arrayContainsItem(arr, "/fldA/fileAA"), is(true));
    }


    private boolean arrayContainsItem(String[] array, String item2Find){
        for(String item : array){
            if (item.equals(item2Find)) return true;
        }
        return false;
    }
    
    private class MemoryStoryHelper implements PointStoreHelper {

        private Map<String, Boolean> fsItems = new HashMap<>();

        public MemoryStoryHelper() {
            fsItems.put("/fldA", true);
            fsItems.put("/file", false);
            fsItems.put("/fldA/fileA", false);
            fsItems.put("/fldA/fileAA", false);
        }

        @Override
        public boolean fileExists(String relativePath) {
            if (!fsItems.containsKey(relativePath)) return false;
            return !fsItems.get(relativePath).booleanValue();
        }

        @Override
        public boolean folderExists(String relativePath) {
            if (!fsItems.containsKey(relativePath)) return false;
            return fsItems.get(relativePath).booleanValue();
        }

        @Override
        public String[] getFiles(String relativePath) {
            Set<String> files = new HashSet<>();
            for(String key: fsItems.keySet() ){
                int fileDelimiterPos = key.lastIndexOf(Constants.JAVA_PATH_DELIMITER);
                String folderName = key.substring(0, fileDelimiterPos);
                if (!relativePath.equals(folderName)) continue;

                if (!fsItems.get(key).booleanValue()){
                    files.add(key);
                }
            }
            return files.isEmpty()
                    ? null
                    : files.toArray(new String[files.size()]);
        }

        @Override
        public String[] getFolders(String relativePath) {
            Set<String> files = new HashSet<>();
            for(String key: fsItems.keySet() ){
                if (!key.startsWith(relativePath)
                        || key.equals(relativePath)) continue;

                if (fsItems.get(key).booleanValue()){
                    files.add(key);
                }
            }
            return files.isEmpty()
                    ? null
                    : files.toArray(new String[files.size()]);
        }

        @Override
        public long getSize(String relativePath) {
            return 0;
        }

        @Override
        public void folderCreate(String relativePath) {
            fsItems.put(relativePath, true);
        }

        @Override
        public String combine(String... paths) {
            String localPath = paths[0];
            if (localPath.equals(""))  localPath = Constants.JAVA_PATH_DELIMITER + "";

            for (int i = 1; i < paths.length; i++) {
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
            return null;
        }

        @Override
        public boolean writeChannel(FileChannel source, String writePath) {
            return false;
        }
    }
}
