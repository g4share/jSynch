import com.g4share.jSynch.share.FileFSHelper;
import com.g4share.jSynch.share.PointStoreHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
* User: gm
* Date: 3/6/12
*/
public class SynchManagerTest {
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

    private void createFile(String tFileName, String text) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tFileName, true))) {
            writer.print(text);
        } catch (IOException e){ throw e;}
    }
}
