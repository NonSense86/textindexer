package at.tuwien.ir.textindexer.weighting;

import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

/**
 * Calculates the weight by using the {@link IndexOutputCollector} and generates
 * the arff file.
 * 
 * @author petar
 * 
 */
public interface WeightingStrategy {

    public void generateOutput(String dir);
}
