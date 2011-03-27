package at.tuwien.ir.textindexer.weighting;

import java.io.IOException;

import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

/**
 * Calculates the weight by using the {@link IndexOutputCollector} and generates
 * the arff file.
 * 
 * @author petar
 * 
 */
public interface WeightingStrategy {

    public void generateOutput(String dir) throws IOException;
    
    /**
     * calculates the weight for the passed word
     * and document. The docname parameter
     * has to be in the form "class/docname".
     * @param word
     * @param docname
     * @return
     */
    public double calcWeight(String word, String docname);
}
