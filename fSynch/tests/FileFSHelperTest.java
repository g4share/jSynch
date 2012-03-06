import com.g4share.jSynch.share.FileFSHelper;
import com.g4share.jSynch.share.PointStoreHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.channels.FileChannel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
* User: gm
* Date: 3/5/12
*/
public class FileFSHelperTest {
    String tPathName;
    PointStoreHelper storeHelper;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        tPathName = folder.getRoot().getAbsolutePath();
        storeHelper = new FileFSHelper(tPathName);
    }

    @Test
    public void testPathCombine() throws Exception {
        String combinedPath = storeHelper.combine("", "/", "/1/", "/2", "3", "\\4\\", "/5");
        assertThat(combinedPath, is("/1/2/3/4/5"));
    }

    @Test
    public void testFileNotExists() throws Exception {
        boolean fileExists = storeHelper.fileExists("file");
        assertThat(fileExists, is(false));
    }

    @Test
    public void testFolderNotExists() throws Exception {
        boolean folderExists = storeHelper.folderExists("folder");
        assertThat(folderExists, is(false));
    }

    @Test
    public void testFolderExists() throws Exception {
        String fldName = "folder";
        boolean folderExists = storeHelper.folderExists(fldName);
        assertThat(folderExists, is(false));

        new File(tPathName + "/" + fldName).mkdirs();

        folderExists = storeHelper.folderExists(fldName);
        assertThat(folderExists, is(true));
    }

    @Test
    public void testFileExists() throws Exception {
        String fn = "file";
        boolean fileExists = storeHelper.fileExists(fn);
        assertThat(fileExists, is(false));

        createFile(tPathName + "/" + fn);
        fileExists = storeHelper.fileExists(fn);
        assertThat(fileExists, is(true));
    }

    @Test
    public void testFileIsNotAFolder() throws Exception {
        String fldName = "folder";
        boolean fileExists = storeHelper.fileExists(fldName);
        assertThat(fileExists, is(false));

        boolean folderExists = storeHelper.folderExists(fldName);
        assertThat(folderExists, is(false));

        storeHelper.folderCreate(fldName);

        fileExists = storeHelper.fileExists(fldName);
        assertThat(fileExists, is(false));

        folderExists = storeHelper.folderExists(fldName);
        assertThat(folderExists, is(true));

        File fld = new File(tPathName + "/" + fldName);
        assertThat(fld.exists(), is(true));
        assertThat(fld.isDirectory(), is(true));
    }

    @Test
    public void testFolderIsNotAFile() throws Exception {
        String fn = "file";
        boolean fileExists = storeHelper.fileExists(fn);
        assertThat(fileExists, is(false));

        boolean folderExists = storeHelper.folderExists(fn);
        assertThat(folderExists, is(false));

        createFile(tPathName + "/" + fn);

        fileExists = storeHelper.fileExists(fn);
        assertThat(fileExists, is(true));

        folderExists = storeHelper.folderExists(fn);
        assertThat(folderExists, is(false));

        File fld = new File(tPathName + "/" + fn);
        assertThat(fld.exists(), is(true));
        assertThat(fld.isFile(), is(true));
    }

    @Test
    public void testCreateFolder() throws Exception {
        String fldName = "folder";
        boolean folderExists = storeHelper.folderExists(fldName);
        assertThat(folderExists, is(false));

        storeHelper.folderCreate(fldName);
        File fld = new File(tPathName + "/" + fldName);

        assertThat(fld.exists(), is(true));
        assertThat(fld.isDirectory(), is(true));
    }

    @Test
    public void testFileSize() throws Exception {
        String fn = "file";
        createFile(tPathName + "/" + fn);
        long fileSize = storeHelper.getSize(fn);
        assertThat(fileSize, is(5L));
    }

    @Test
    public void testFileContent() throws Exception {
        String fn = "file";
        createFile(tPathName + "/" + fn);
        String content = readFileAsString(tPathName + "/" + fn);

        assertThat(content, is("12345"));
    }

    @Test
    public void testEmptyRootList() throws Exception {
        String[] files = storeHelper.getFiles(tPathName);
        String[] folders = storeHelper.getFolders(tPathName);
        assertThat(files, nullValue());
        assertThat(folders, nullValue());
    }

    @Test
    public void testUnexistedPathList() throws Exception {
        String[] files = storeHelper.getFiles("no path");
        String[] folders = storeHelper.getFolders("no path");
        assertThat(files, nullValue());
        assertThat(folders, nullValue());
    }

    @Test
    public void testFullList() throws Exception {
        createFile(tPathName + "/rootFile1");
        createFile(tPathName + "/rootFile2");

        String[] files = storeHelper.getFiles("/");
        String[] folders = storeHelper.getFolders("/");

        assertThat(files.length, is(2));
        assertThat(folders, nullValue());
        
        boolean found = arrayContainsItem(files, "rootFile1");
        assertThat(found, is(true));

        found = arrayContainsItem(files, "rootFile2");
        assertThat(found, is(true));
    }

    @Test
    public void testCustomPathList() throws Exception {
        storeHelper.folderCreate("2levelFolder");
        createFile(tPathName + "/2levelFolder/2levelFile");

        String[] folders = storeHelper.getFolders("");
        String[] files = storeHelper.getFiles("2levelFolder");

        assertThat(files.length, is(1));
        assertThat(folders.length, is(1));

        boolean found = arrayContainsItem(files, "2levelFile");
        assertThat(found, is(true));

        found = arrayContainsItem(folders, "2levelFolder");
        assertThat(found, is(true));
    }

    @Test
    public void testCopyFile() throws Exception {
        String fn = "file";
        storeHelper.folderCreate("folder");

        createFile(tPathName + "/" + fn);
        String contentFrom = readFileAsString(tPathName + "/" + fn);

        FileChannel source = null;
        try{
            source = storeHelper.getReadChannel("/" + fn);
            storeHelper.writeChannel(source, "/folder/copy")  ;
        }finally {
            if (source != null) source.close();
        }

        String contentTo = readFileAsString(tPathName + "/folder/copy");
        assertThat(contentTo, is(contentFrom));
    }


    private boolean arrayContainsItem(String[] array, String item2Find){
        for(String item : array){
            if (item.equals(item2Find)) return true;
        }
        return false;
    }

    private void createFile(String tFileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tFileName, true))) {
            writer.print("12345");
        } catch (IOException e){ throw e;}
    }

    private static String readFileAsString(String filePath) throws java.io.IOException{
        byte[] buffer = new byte[(int) new File(filePath).length()];
        try(BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath))) {
            f.read(buffer);
        }
        return new String(buffer);
    }
}
