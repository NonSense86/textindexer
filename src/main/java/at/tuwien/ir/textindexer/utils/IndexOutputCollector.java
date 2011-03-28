package at.tuwien.ir.textindexer.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A singleton that collects the results of the map reduce procedure.
 * @author petar
 *
 */
public class IndexOutputCollector {

    private static IndexOutputCollector uniqueInstance;
    
    private Map<String, IndexCount> outputMap;
    
    private Map<String, Integer> inputFiles;
    
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
            this.outputMap.put(doc, count);
    }
    
    public synchronized void addDoc(String doc, Integer i) {
        this.inputFiles.put(doc, i);
    }
    
    public synchronized Map<String, IndexCount> getOutputMap() {
        return outputMap;
    }

    public Map<String, Integer> getInputFiles() {
        return inputFiles;
    }

    private IndexOutputCollector() {
        this.outputMap = new ConcurrentHashMap<String, IndexCount>();
        this.inputFiles = new HashMap<String, Integer>();
    }

}
