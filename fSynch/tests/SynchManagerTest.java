import com.g4share.jSynch.share.*;
import com.g4share.jSynch.share.service.Constants;
import com.g4share.jSynch.share.service.PointStoreHelper;
import com.g4share.jSynch.share.service.SynchManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
* User: gm
* Date: 3/6/12
*/
public class SynchManagerTest {
    SynchManager synchManager;

    String tPathName;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        tPathName = folder.getRoot().getAbsolutePath();
    }


    @Test
    public void testSetUnexistentFolder() throws Exception {
        CommonTestMethods.createFile(tPathName + "/file");

        synchManager = new FSSynchManager(null);

        MemoryStoryHelper pointStoreA = new MemoryStoryHelper("A");
        MemoryStoryHelper pointStoreB = new MemoryStoryHelper("B");

        setFolder(pointStoreA, pointStoreB, synchManager.getFolder(pointStoreA));
        setFolder(pointStoreB, pointStoreA, synchManager.getFolder(pointStoreB));

        checkSynch(pointStoreA);
        checkSynch(pointStoreB);
    }

    private void checkSynch(MemoryStoryHelper pointStore){
        assertThat(pointStore.fsItems.size(), is(8));

        assertThat(pointStore.folderExists("/"), is(true));
        assertThat(pointStore.folderExists("/fldA"), is(true));
        assertThat(pointStore.folderExists("/fldB"), is(true));

        assertThat(pointStore.fileExists("/file"), is(true));

        assertThat(pointStore.fileExists("/fldA/fileA"), is(true));
        assertThat(pointStore.fileExists("/fldA/fileAA"), is(true));

        assertThat(pointStore.fileExists("/fldB/fileB"), is(true));
        assertThat(pointStore.fileExists("/fldB/fileBB"), is(true));

    }
        
    private void setFolder(PointStoreHelper pointStoreFrom,
                           PointStoreHelper pointStoreTo,
                           SynchFolder synchFolder) {
        Constants.Codes result = synchManager.setFolder(pointStoreTo, synchFolder.getRelativePath());

        //if folder could not be created, no synchronization for this path
        if (result == Constants.Codes.FATAL_ERROR_CODE){
            return;
        }
        
        if (synchFolder.getFolders() != null
                && synchFolder.getFolders().length > 0){
            
            for(SynchFolder innerFolder : synchFolder.getFolders()) {
                setFolder(pointStoreFrom, pointStoreTo, innerFolder);
            }
        }

        SynchFile[] innerFiles = synchFolder.getFiles();
        if (innerFiles == null) return;

        for(SynchFile innerFile : innerFiles) {
            switch(synchManager.checkFile(pointStoreTo, innerFile)){
                case FATAL_ERROR_CODE :
                    return;
                case ERROR_CODE :
                    FileChannel source = pointStoreFrom.getReadChannel(innerFile.getName());
                    synchManager.setFile(pointStoreTo, innerFile, source);
            }
        }
    }

    @Test
    public void testGetItems() throws Exception {

        synchManager = new FSSynchManager(null);

        SynchFolder folder = synchManager.getFolder(new MemoryStoryHelper("A"));

        assertThat(folder.getRelativePath(), is("/"));
        assertThat(folder.getFiles().length, is(1));
        assertThat(folder.getFiles()[0].getName(), is("/file"));

        assertThat(folder.getFolders().length, is(1));
        assertThat(folder.getFolders()[0].getRelativePath(), is("/fldA"));
        assertThat(folder.getFolders()[0].getFiles().length, is(2));

        String[] arr = new String[] {folder.getFolders()[0].getFiles()[0].getName(),
                folder.getFolders()[0].getFiles()[1].getName()};
        assertThat(CommonTestMethods.arrayContainsItem(arr, "/fldA/fileA"), is(true));
        assertThat(CommonTestMethods.arrayContainsItem(arr, "/fldA/fileAA"), is(true));

        assertThat(folder.getFolders()[0].getFolders(), nullValue());
    }

    
    private class MemoryStoryHelper implements PointStoreHelper {
        private Map<String, Boolean> fsItems = new HashMap<>();

        public MemoryStoryHelper(String folderLetter) {
            fsItems.put("/", true);
            fsItems.put("/fld" + folderLetter, true);
            fsItems.put("/file", false);
            fsItems.put("/fld" + folderLetter + "/file" + folderLetter, false);
            fsItems.put("/fld" + folderLetter + "/file" + folderLetter + folderLetter, false);
        }

        @Override
        public boolean fileExists(String relativePath) {
            if (!fsItems.containsKey(relativePath)) return false;
            return !fsItems.get(relativePath);
        }

        @Override
        public boolean folderExists(String relativePath) {
            if (!fsItems.containsKey(relativePath)) return false;
            return fsItems.get(relativePath);
        }

        @Override
        public String[] getFiles(String relativePath) {
            Set<String> files = new HashSet<>();
            for(String key: fsItems.keySet() ){
                int fileDelimiterPos = key.lastIndexOf(Constants.JAVA_PATH_DELIMITER);
                String folderName = key.substring(0, fileDelimiterPos);
                if (folderName.equals("")) {
                    folderName = Constants.JAVA_PATH_DELIMITER + "";
                }
                if (!relativePath.equals(folderName)) continue;

                if (!fsItems.get(key)){
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

                if (fsItems.get(key)){
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

                String processedPath = paths[i].replace(Constants.WIN_PATH_DELIMITER, Constants.JAVA_PATH_DELIMITER);

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
            try {
                return new FileInputStream(tPathName + "/file").getChannel();
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        @Override
        public boolean writeChannel(FileChannel source, String writePath) {
            fsItems.put(writePath, false);
            return true;
        }
    }
}
