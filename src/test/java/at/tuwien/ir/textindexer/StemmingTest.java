package at.tuwien.ir.textindexer;

import java.io.File;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.mapred.AggregatorJob;
import at.tuwien.ir.textindexer.mapred.IndexingJob;
import at.tuwien.ir.textindexer.mapred.MapRedJob;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;
import at.tuwien.ir.textindexer.utils.TestUtilities;

public class StemmingTest {

    @Before
    public void setup() {
        TestUtilities.deleteFolder(new File(Constants.TMP_OUTPUT_PATH+"_indexing"));
        TestUtilities.deleteFolder(new File(Constants.TMP_OUTPUT_PATH+"_wcounting"));
    }
    
    @After
    public void teardown() {
        TestUtilities.deleteFolder(new File(Constants.TMP_OUTPUT_PATH+"_indexing"));
        TestUtilities.deleteFolder(new File(Constants.TMP_OUTPUT_PATH+"_wcounting"));
        IndexOutputCollector.getInstance().getOutputMap().clear();
    }
    
    @Test
    public void shallStem() throws Exception {
        ConfigUtils.loadConfig(new File("src/test/resources/test_config.properties"));
        MapRedJob indexing = new IndexingJob("src/test/resources/testStem/");
        indexing.run();
        
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();

        Assert.assertEquals(11, map.keySet().size());
        
        Assert.assertTrue(map.containsKey("stem"));
        Assert.assertFalse(map.containsKey("stemming"));
        
        Assert.assertTrue(map.containsKey("dog"));
        Assert.assertFalse(map.containsKey("dogs"));
        
        Assert.assertTrue(map.containsKey("plai"));
        Assert.assertFalse(map.containsKey("playing"));
    }
}
