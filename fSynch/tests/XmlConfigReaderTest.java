import com.g4share.jSynch.config.*;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.service.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * User: gm
 * Date: 3/4/12
 */
public class XmlConfigReaderTest {
    Logger logger;
    String fatalMessage;
    String errorMessage;

    @Before
    public void setUp() throws Exception {
        logger = new Logger() {
            LogLevel level;
            @Override
            public void logEvent(LogLevel level, String message) {
                if (level == LogLevel.FATAL) { fatalMessage = message; }
                if (level == LogLevel.ERROR) { errorMessage = message; }
            }

            @Override
            public void setLevel(LogLevel level) {
                this.level = level;
            }
        };

        fatalMessage = errorMessage = null;
    }

    @Test
    public void testReadError() throws Exception {
        TestXmlReader testXmlReader = new TestXmlReader() {
            @Override
            public Constants.Codes read(String path) {
                configStore.AddNode(XmlNode.NONE, null);
                return Constants.Codes.ERROR_CODE;
            }
        };
        testXmlReader.setStore(new ConfigStorage(logger));
        ConfigReader configReader = new XmlConfigReader(logger, testXmlReader);

        ConfigInfo cInfo = configReader.read(null);
        assertThat(cInfo, nullValue());
        assertThat(fatalMessage, containsString("interval"));
    }

    @Test
    public void testNotEnoughtPoints() throws Exception {
        final int rnd = new Random().nextInt();
        TestXmlReader testXmlReader = new TestXmlReader() {
            @Override
            public Constants.Codes read(String path) {
                Map<String, String> attributes = new HashMap<>();

                attributes.put(Constants.SECONDS_ATTRIBUTE, Integer.toString(rnd));

                attributes.put(Constants.NAME_ATTRIBUTE, "pointA");
                attributes.put(Constants.VALUE_ATTRIBUTE, "pointA pathA");

                configStore.AddNode(XmlNode.INTERVAL, attributes);
                configStore.AddNode(XmlNode.PATH, attributes);

                return Constants.Codes.SUCCESS_CODE;
            }
        };

        testXmlReader.setStore(new ConfigStorage(logger));
        ConfigReader configReader = new XmlConfigReader(logger, testXmlReader);

        ConfigInfo cInfo = configReader.read(null);
        assertThat(cInfo, nullValue());

        assertThat(errorMessage, containsString("at least 2 points"));
        assertThat(fatalMessage, containsString("There are no points"));
    }


    @Test
    public void testRead() throws Exception {
        final int rnd = new Random().nextInt(1000);
        TestXmlReader testXmlReader = new TestXmlReader() {
            @Override
            public Constants.Codes read(String path) {
                Map<String, String> attributes = new HashMap<>();

                attributes.put(Constants.SECONDS_ATTRIBUTE, Integer.toString(rnd));
                configStore.AddNode(XmlNode.INTERVAL, attributes);

                attributes.clear();
                attributes.put(Constants.NAME_ATTRIBUTE, "pointA");
                attributes.put(Constants.VALUE_ATTRIBUTE, "pointA pathA");
                configStore.AddNode(XmlNode.PATH, attributes);

                attributes.clear();
                attributes.put(Constants.NAME_ATTRIBUTE, "pointA");
                attributes.put(Constants.VALUE_ATTRIBUTE, "pointA pathAA");
                configStore.AddNode(XmlNode.PATH, attributes);

                return Constants.Codes.SUCCESS_CODE;
            }
        };

        testXmlReader.setStore(new ConfigStorage(logger));
        ConfigReader configReader = new XmlConfigReader(logger, testXmlReader);

        ConfigInfo cInfo = configReader.read(null);
        assertThat(cInfo, notNullValue());

        assertThat(errorMessage, nullValue());
        assertThat(fatalMessage, nullValue());
        assertThat(cInfo.getInterval(), is(rnd * 1000));

        assertThat(cInfo.getPointInfo().length, is(1));
    }

    private abstract class TestXmlReader implements XmlReader {
        ConfigStore configStore;

        @Override
        public void setStore(ConfigStore configStore) {
            this.configStore = configStore;
        }

        @Override
        public ConfigStore getStore() {
            return configStore;
        }

        @Override
        public abstract Constants.Codes read(String path);
    }
}
