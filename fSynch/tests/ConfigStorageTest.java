import com.g4share.jSynch.config.ConfigStorage;
import com.g4share.jSynch.config.ConfigStore;
import com.g4share.jSynch.config.XmlNode;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.service.Constants;
import com.g4share.jSynch.share.PointInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
* User: gm
* Date: 3/4/12
*/
public class ConfigStorageTest {
    ConfigStore store;
    boolean fatal;

    @Before
    public void setUp() throws Exception {
        store = new ConfigStorage(new Logger() {
            LogLevel level;

            @Override
            public void setLevel(LogLevel level) {
                this.level = level;
            }

            @Override
            public void logEvent(LogLevel level, String message) {
                if (level == LogLevel.FATAL) fatal = true;
            }
        });

        fatal = false;
    }

    @Test
    public void testInitialStateIsClear() throws Exception {
        assertThat(store.getInterval(), is(0));
        assertThat(store.getPoints().length, is(0));
        assertThat(fatal, is(false));
    }

    @Test
    public void testWrongNodeAdded() throws Exception {
        assertThat(store.getInterval(), is(0));
        store.AddNode(XmlNode.NONE, null);

        assertThat(store.getInterval(), is(0));
        assertThat(store.getPoints().length, is(0));
        assertThat(fatal, is(false));
    }

    @Test
    public void testInconsistentIntervalAdded() throws Exception {
        assertThat(store.getInterval(), is(0));
        assertThat(fatal, is(false));
        store.AddNode(XmlNode.INTERVAL, null);
        assertThat(store.getInterval(), is(Constants.WRONG_NUMBER));
        assertThat(fatal, is(true));
    }

    @Test
    public void testWrongFormatIntervalAdded() throws Exception {
        assertThat(store.getInterval(), is(0));
        assertThat(fatal, is(false));

        Map<String, String> attributes = new HashMap<>();
        attributes.put(Constants.SECONDS_ATTRIBUTE, "no int value");
        store.AddNode(XmlNode.INTERVAL, attributes);

        assertThat(store.getPoints().length, is(0));
        assertThat(store.getInterval(), is(Constants.WRONG_NUMBER));
        assertThat(fatal, is(true));
    }

    @Test
    public void testIntervalAdded() throws Exception {
        assertThat(store.getInterval(), is(0));
        assertThat(fatal, is(false));

        Map<String, String> attributes = new HashMap<>();

        attributes.put(Constants.SECONDS_ATTRIBUTE, "47");
        store.AddNode(XmlNode.INTERVAL, attributes);

        assertThat(store.getPoints().length, is(0));
        assertThat(store.getInterval(), is(47));
        assertThat(fatal, is(false));
    }

    @Test
    public void testPointAPathAAdded() throws Exception {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(Constants.NAME_ATTRIBUTE, "pointA");
        attributes.put(Constants.VALUE_ATTRIBUTE, "pointA pathA");
        store.AddNode(XmlNode.PATH, attributes);

        assertThat(store.getPoints().length, is(1));

        PointInfo pInfoA = store.getPoints()[0];
        assertThat(pInfoA.getName(), is("pointA"));
        assertThat(pInfoA.getStorePaths().length, is(1));
        assertThat(pInfoA.getStorePaths()[0], is("pointA pathA"));
        assertThat(fatal, is(false));
    }

    @Test
    public void testPointAPathAPointBPathBPathCAdded() throws Exception {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(Constants.NAME_ATTRIBUTE, "pointA");
        attributes.put(Constants.VALUE_ATTRIBUTE, "pointA pathA");
        store.AddNode(XmlNode.PATH, attributes);

        attributes.clear();
        attributes.put(Constants.NAME_ATTRIBUTE, "pointB");
        attributes.put(Constants.VALUE_ATTRIBUTE, "pointB pathB");
        store.AddNode(XmlNode.PATH, attributes);

        attributes.clear();
        attributes.put(Constants.NAME_ATTRIBUTE, "pointB");
        attributes.put(Constants.VALUE_ATTRIBUTE, "pointB pathBB");
        store.AddNode(XmlNode.PATH, attributes);


        assertThat(store.getPoints().length, is(2));

        PointInfo[] points = store.getPoints();

        PointInfo pInfoA = null;
        for(PointInfo tInfo : points) {
            if (tInfo.getName().equals("pointA")) {
                pInfoA = tInfo;
                break;
            }
        }

        assertThat(pInfoA, notNullValue());
        assertThat(pInfoA.getStorePaths().length, is(1));
        assertThat(pInfoA.getStorePaths()[0], is("pointA pathA"));


        PointInfo pInfoB = null;
        for(PointInfo tInfo : points) {
            if (tInfo.getName().equals("pointB")) {
                pInfoB = tInfo;
                break;
            }
        }

        assertThat(pInfoB.getStorePaths().length, is(2));
        assertThat(pInfoB.getStorePaths()[0], is("pointB pathB"));
        assertThat(pInfoB.getStorePaths()[1], is("pointB pathBB"));

        assertThat(fatal, is(false));
    }
}
