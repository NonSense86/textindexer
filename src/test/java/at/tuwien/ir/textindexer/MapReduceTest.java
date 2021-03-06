package at.tuwien.ir.textindexer;

import java.io.File;
import java.util.Map;

import junit.framework.Assert;

import org.apache.hadoop.io.Text;
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

public class MapReduceTest {


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
    public void shallCountWords() throws Exception {
        String defaultConf = null;
        ConfigUtils.loadConfig(defaultConf);
        MapRedJob indexing = new IndexingJob("src/test/resources/test");
        indexing.run();
        
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        Assert.assertEquals(6, map.size());
        Assert.assertTrue(map.containsKey("hello"));
        Assert.assertTrue(map.containsKey("world"));
        Assert.assertTrue(map.containsKey("map"));
        Assert.assertTrue(map.containsKey("reduce"));
        Assert.assertTrue(map.containsKey("foo"));
        Assert.assertTrue(map.containsKey("bar"));
        Assert.assertFalse(map.containsKey("nonexisting"));
        
        IndexCount ic = map.get("hello");
        Assert.assertNotNull(ic);
        Assert.assertEquals(2, ic.getDocFrequency());
        Assert.assertNotNull(ic.getTermFrequency());
        Assert.assertSame(2, ic.getTermFrequency().keySet().size());
        Assert.assertNotNull(ic.getTermFrequency().get(new Text("testB/test2.txt")));
        Assert.assertEquals(2, ic.getTermFrequency().get(new Text("testB/test2.txt")).get());
        
        ic = map.get("reduce");
        Assert.assertNotNull(ic);
        Assert.assertEquals(1, ic.getDocFrequency());
        Assert.assertNotNull(ic.getTermFrequency());
        Assert.assertSame(1, ic.getTermFrequency().keySet().size());
        Assert.assertNotNull(ic.getTermFrequency().get(new Text("testB/test2.txt")));
        Assert.assertEquals(3, ic.getTermFrequency().get(new Text("testB/test2.txt")).get());
        
    }
}
