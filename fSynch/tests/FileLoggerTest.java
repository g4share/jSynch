import com.g4share.jSynch.log.FileLogger;
import com.g4share.jSynch.log.LogLevel;
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
        logger.setLevel(LogLevel.TRACE);
        logger.logEvent(LogLevel.INFO, "it happens...");
        File logFile = new File(properties.getFileName());
        assertThat(logFile.exists(), is(true));
    }

    @Test
    public void logEvent() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.TRACE);
        logger.logEvent(LogLevel.INFO, text);

        String line = CommonTestMethods.getLine(properties.getFileName(), 0);
        assertThat(line, endsWith(text));
    }

    @Test
    public void logError() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.TRACE);
        logger.logEvent(LogLevel.ERROR, text);

        String line = CommonTestMethods.getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("* "));
        assertThat(line, endsWith(text));
    }

    @Test
    public void logFatal() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.TRACE);
        logger.logEvent(LogLevel.FATAL, text);

        String line = CommonTestMethods.getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("! "));
        assertThat(line, endsWith(text));
    }

    @Test
    public void logDisabledLevel() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.FATAL);
        logger.logEvent(LogLevel.TRACE, text);

        File logFile = new File(properties.getFileName());
        assertThat(logFile.exists(), is(false));
    }

    @Test
    public void logSameLevel() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.FATAL);
        logger.logEvent(LogLevel.FATAL, text);

        File logFile = new File(properties.getFileName());
        assertThat(logFile.exists(), is(true));

        String line = CommonTestMethods.getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("! "));
        assertThat(line, endsWith(text));
    }

    @Test
    public void logEnabledLevel() throws Exception{
        String text = "it happens...";

        Logger logger = new FileLogger(properties, null);
        logger.setLevel(LogLevel.INFO);
        logger.logEvent(LogLevel.FATAL, text);

        File logFile = new File(properties.getFileName());
        assertThat(logFile.exists(), is(true));

        String line = CommonTestMethods.getLine(properties.getFileName(), 0);
        assertThat(line, startsWith("! "));
        assertThat(line, endsWith(text));
    }

}
