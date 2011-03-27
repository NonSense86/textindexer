package at.tuwien.ir.textindexer.weighting;

import java.util.Map;

import org.apache.hadoop.io.IntWritable;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class TermFrequencyWeightingStrategy extends AbstractWeightingStrategy {

	public TermFrequencyWeightingStrategy() {
		filePrefix = "tfw";
	}
	
    public void generateOutput(String dir) {
        // TODO Auto-generated method stub
        
    }

    public float calcWeight(String word, String docname) {
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        IndexCount ic = map.get(word);
        
        if (ic.getTermFrequency().containsKey(docname)) {
            return (float) ic.getTermFrequency().get(docname).get();
        }
        
        return 0f;
    }


}
