import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlFileReader;
import com.g4share.jSynch.config.XmlNode;
import com.g4share.jSynch.config.XmlReader;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.share.service.Constants;
import com.g4share.jSynch.share.PointInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
* User: gm
* Date: 3/4/12
*/
public class XmlFileReaderTest {
    XmlReader xmlReader;
    String tFileName;

    private Map<String, String> points;
    private String intervalRaw;
    private String logLevelRaw;
    
    private String error;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        points = new TreeMap<>();
        tFileName = folder.getRoot().getAbsolutePath() + "/config";
        CommonTestMethods.createConfigFile(tFileName);
        
        xmlReader = new XmlFileReader();

        xmlReader.setStore(new ConfigStore() {
            @Override
            public void AddNode(XmlNode node, Map<String, String> attributes) {
                switch(node){
                    case INTERVAL:
                        intervalRaw = CommonTestMethods.GetValue(attributes, Constants.SECONDS_ATTRIBUTE);
                        break;
                    case PATH:
                        points.put(CommonTestMethods.GetValue(attributes, Constants.NAME_ATTRIBUTE),
                                CommonTestMethods.GetValue(attributes, Constants.VALUE_ATTRIBUTE));
                        break;
                    case LOG:
                        logLevelRaw = CommonTestMethods.GetValue(attributes, Constants.LOG_LEVEL_ATTRIBUTE);
                        break;

                }

                error = null;
            }

            @Override
            public void EventOccurred(LogLevel level, String hint) {
                error = hint;
            }

            @Override
            public int getInterval() {
                throw new NotImplementedException();
            }

            @Override
            public PointInfo[] getPoints() {
                throw new NotImplementedException();
            }

            @Override
            public LogLevel getLogLevel() {
                return LogLevel.TRACE;
            }
        });
    }

    @Test
    public void testReadInexistentFile() throws Exception {
        assertThat(error, nullValue());
        Constants.Codes result = xmlReader.read(tFileName + "/non existent file.name");
        assertThat(result, is(Constants.Codes.FATAL_ERROR_CODE));

        assertThat(error, containsString("does not exists"));

        assertThat(intervalRaw, nullValue());
        assertThat(points.size(), is(0));
    }

    @Test
    public void testReadNonXmlFile() throws Exception {
        assertThat(error, nullValue());
        //duplicate the text ie file is not more a valid xml one
        CommonTestMethods.createConfigFile(tFileName);

        Constants.Codes result = xmlReader.read(tFileName);
        assertThat(result, is(Constants.Codes.FATAL_ERROR_CODE));

        assertThat(error, containsString("possible wrong format"));
    }

    @Test
    public void testReadFromFile() throws Exception {
        Constants.Codes result = xmlReader.read(tFileName);
        assertThat(result, is(Constants.Codes.SUCCESS_CODE));

        assertThat(intervalRaw, is("*"));
        assertThat(logLevelRaw, is("LL"));
        assertThat(points.size(), is(4));

        assertThat(points.get("sh1"), is("/synch1/"));
        assertThat(points.get("sh1_"), is("/synch2/"));

        assertThat(points.get("sh2"), is("/synch3/"));
        assertThat(points.get("sh2_"), is("/synch4/"));

        assertThat(error, nullValue());
    }
}
