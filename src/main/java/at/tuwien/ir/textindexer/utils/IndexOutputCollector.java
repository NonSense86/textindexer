package at.tuwien.ir.textindexer.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Text;

public class IndexOutputCollector {

    private static IndexOutputCollector uniqueInstance;
    
    private Map<String, IndexCount> outputMap;
    
    private Set<String> inputFiles;
    
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
    
    public synchronized void addResult(String doc, IndexCount count) {
//        if (ic == null) {
//            IndexCount c = new IndexCount();
//            c.setTermFrequency(count.getTermFrequency());
            this.outputMap.put(doc, count);
//        } else {
//            for (Text t : count.getTermFrequency().keySet()) {
//                ic.incrTermFrequency(t.toString(), count.getTermFrequency().get(t).get());
//            }
//        }
    }
    
    public synchronized Map<String, IndexCount> getOutputMap() {
        return outputMap;
    }

    public Set<String> getInputFiles() {
        return inputFiles;
    }

    private IndexOutputCollector() {
        this.outputMap = new HashMap<String, IndexCount>();
        this.inputFiles = new HashSet<String>();
    }

}
