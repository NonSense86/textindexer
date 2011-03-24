package at.tuwien.ir.textindexer;

import java.io.File;
import java.util.Map;

import junit.framework.Assert;

import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.mapred.CounterJob;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class MapReduceTest {


    @Before
    public void setup() {
        this.deleteFolder(new File(Constants.TMP_OUTPUT_PATH));
    }
    
    private boolean deleteFolder(final File path) {
        if (path.exists()) {
            final File[] files = path.listFiles();
            for (final File f : files) {
                if (f.isDirectory()) {
                    this.deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        
        return path.delete();
    }

    @Test
    public void shallCountWords() throws Exception {
        CounterJob cj = new CounterJob();
        cj.doJob("src/test/resources/test");
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
