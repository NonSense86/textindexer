package at.tuwien.ir.textindexer.weighting;

import java.util.Map;

import org.apache.hadoop.io.Text;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class TermFrequencyWeightingStrategy extends AbstractWeightingStrategy {

	public TermFrequencyWeightingStrategy() {
		filePrefix = "tfw";
	}
	
    public double calcWeight(String word, String docname) {
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        IndexCount ic = map.get(word);
        
        Text key = new Text(docname);
        if (ic.getTermFrequency().containsKey(key)) {
            return (double) ic.getTermFrequency().get(key).get();
        }
        
        return 0d;
    }


}
