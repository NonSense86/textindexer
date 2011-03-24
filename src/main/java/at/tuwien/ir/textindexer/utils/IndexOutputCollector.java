package at.tuwien.ir.textindexer.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;

public class IndexOutputCollector {

    private static IndexOutputCollector uniqueInstance;
    
    private Map<String, IndexCount> outputMap;
    
    public static synchronized IndexOutputCollector getInstance() {
        if (IndexOutputCollector.uniqueInstance == null) {
            IndexOutputCollector.uniqueInstance = new IndexOutputCollector();
        }
        
        return IndexOutputCollector.uniqueInstance;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone a singleton instance");
    }
    
    public void addResult(String doc, IndexCount count) {
        IndexCount ic = this.outputMap.get(doc);
        if (ic == null) {
            this.outputMap.put(doc, count);
        } else {
            ic.incrDocFrequency(count.getDocFrequency());
            for (Text t : count.getTermFrequency().keySet()) {
                ic.incrTermFrequency(t.toString(), count.getTermFrequency().get(t).get());
            }
        }
    }
    
    public Map<String, IndexCount> getOutputMap() {
        return outputMap;
    }

    private IndexOutputCollector() {
        this.outputMap = new HashMap<String, IndexCount>();
    }
}
