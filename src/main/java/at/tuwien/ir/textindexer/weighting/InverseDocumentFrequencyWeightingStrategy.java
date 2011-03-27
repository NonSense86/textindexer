package at.tuwien.ir.textindexer.weighting;

import java.util.Map;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class InverseDocumentFrequencyWeightingStrategy extends AbstractWeightingStrategy {

	public InverseDocumentFrequencyWeightingStrategy() {
		filePrefix = "idfw";
	}
	
    public void generateOutput(String dir) {
        // TODO Auto-generated method stub
        
    }

    public float calcWeight(String word, String docname) {
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        int N = IndexOutputCollector.getInstance().getInputFiles().size();
        int dfi = map.get(word).getDocFrequency();
        int tfij = 0;
        
        IndexCount ic = map.get(word);
        
        if (ic.getTermFrequency().containsKey(docname)) {
            tfij = ic.getTermFrequency().get(docname).get();
        }
        
        //TODO ... not working yet..
        
        return -1f;
    }


}
