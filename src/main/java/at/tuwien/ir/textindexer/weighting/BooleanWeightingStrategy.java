package at.tuwien.ir.textindexer.weighting;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class BooleanWeightingStrategy extends AbstractWeightingStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooleanWeightingStrategy.class);

    public BooleanWeightingStrategy() {
    	filePrefix = "bw";
    }
    
    public double calcWeight(String word, String docname) {
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        IndexCount ic = map.get(word);
        
        return (ic.getTermFrequency().containsKey(docname)) ? 1d : 0d; 
    }
    
    


}
