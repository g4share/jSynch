import com.g4share.jSynch.log.FileLogger;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.log.LoggerProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * User: gm
 * Date: 3/3/12
 */
public class FileLoggerTest {
    private LoggerProperties properties;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        properties = new LoggerProperties() {
            @Override
            public String getFileName() {
                return folder.getRoot().getAbsolutePath() + "/log";
            }
        };

    }

    @After
    public void tearDown() throws Exception { }

    @Test
    public void create_New_Log() {
        Logger logger = new FileLogger(properties, null);
        logger.logEvent("it happens...");
        File logFile = new File(properties.getFileName());
        assertThat(logFile.exists(), is(true));
    }

    @Test
    public void logEvent() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.logEvent(text);

        String line = getLine(properties.getFileName(), 0);
        assertThat(line, endsWith(text));
    }

    @Test
    public void logError() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.logError(text);

        String line = getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("* "));
        assertThat(line, endsWith(text));
    }

    @Test
    public void logFatal() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.logFatal(text);

        String line = getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("! "));
        assertThat(line, endsWith(text));
    }

    private String getLine(String fileName, int lineNumber) throws Exception{
        int currentLineNumber = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))))) {
            String line;
            while ((line = br.readLine()) != null)   {
                if (currentLineNumber++ == lineNumber) return line;
                return line;
            }

        };
        throw new Exception("Could not find the line " + lineNumber);
    }
}
