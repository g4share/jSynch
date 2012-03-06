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
    String tPathNameRoot;
    SynchManager syncManager;

    //@Rule
    //public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        //tPathNameRoot = folder.getRoot().getAbsolutePath();
        //tPathNameA = tPathNameRoot + "/fldA";
        //tPathNameB = tPathNameRoot + "/fldB";
    }

    @Test
    public void testPathCombine() throws Exception {
//        new File(tPathNameRoot + "/fldA").mkdirs();
//        new File(tPathNameRoot + "/fldA/fldAA").mkdirs();
        
//        createFile(tPathNameRoot + "/fldA/fileA1", "fileA1");
//        createFile(tPathNameRoot + "/fldA/fldAA/fileAA1", "fileAA1");

        syncManager = new FSSynchManager("/", null);

        SynchFolder folder = syncManager.getFolder(new MemoryStoryHelper());
//        assertThat(combinedPath, is("/1/2/3/4/5"));
    }

    private void createFile(String tFileName, String text) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tFileName, true))) {
            writer.print(text);
        } catch (IOException e){ throw e;}
    }
    
    private class MemoryStoryHelper implements PointStoreHelper {

        private Map<String, Boolean> fsItems = new HashMap<>();

        public MemoryStoryHelper() {
            fsItems.put("/fldA", true);
            fsItems.put("/file", false);
            fsItems.put("/fldA/fileA", false);
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
                if (fsItems.get(key).booleanValue()){
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
                if (!fsItems.get(key).booleanValue()){
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
            return paths[0];
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
