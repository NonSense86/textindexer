package at.tuwien.ir.textindexer.weighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanWeightingStrategy extends AbstractWeightingStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BooleanWeightingStrategy.class);

    public BooleanWeightingStrategy() {
    	filePrefix = "bw";
    }
    
    public void generateOutput(String dir) {
        // TODO Auto-generated method stub
        
    }

    public float calcWeight(String word, String docname) {
        return 0;
    }
    
    


}
