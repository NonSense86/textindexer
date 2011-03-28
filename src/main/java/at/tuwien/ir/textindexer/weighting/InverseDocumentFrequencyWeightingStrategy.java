package at.tuwien.ir.textindexer.weighting;

import java.util.Map;

import org.apache.hadoop.io.Text;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class InverseDocumentFrequencyWeightingStrategy extends AbstractWeightingStrategy {

	public InverseDocumentFrequencyWeightingStrategy() {
		filePrefix = "idfw";
	}
	
    public double calcWeight(String word, String docname) {
        Text key = new Text(docname);
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        int N = IndexOutputCollector.getInstance().getInputFiles().size();
        int dfi = map.get(word).getDocFrequency();
        int tfkj =IndexOutputCollector.getInstance().getInputFiles().get(docname);
        double tfij = 0;
        
        IndexCount ic = map.get(word);
        if (ic.getTermFrequency().containsKey(key)) {
            tfij = ic.getTermFrequency().get(key).get();
        }
        
        double log = Math.log(((double) N / dfi));
        return (tfij / tfkj) * log;
        
    }


}
