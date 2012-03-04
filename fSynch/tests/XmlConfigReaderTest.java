import com.g4share.jSynch.config.*;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
* Created by IntelliJ IDEA.
* User: gm
* Date: 3/4/12
* Time: 12:52 AM
* To change this template use File | Settings | File Templates.
*/
public class XmlConfigReaderTest {
    ConfigReader configReader;

    @Before
    public void setUp() throws Exception {
        configReader = new XmlConfigReader(new Logger() {
            @Override
            public void logEvent(String message) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void logError(String exception) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void logFatal(String exception) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }, new TestXmlReader());
    }

    @Test
    public void testRead() throws Exception {
        ConfigInfo cInfo = configReader.read(null);
        assertThat(cInfo.getInterval(), is(0));
        assertThat(cInfo.getPointInfo().length, is(0));
    }

    private class TestXmlReader implements XmlReader {
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
        public Constants.Codes read(String path) {
            configStore.AddNode(XmlNode.none, null);
            return Constants.Codes.SUCCESS_CODE;
        }
    }
}
