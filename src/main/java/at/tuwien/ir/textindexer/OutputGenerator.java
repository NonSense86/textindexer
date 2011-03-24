package at.tuwien.ir.textindexer;

import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

public class OutputGenerator {

    private WeightingStrategy strategy;
    
    public OutputGenerator(WeightingStrategy st) {
        this.setStrategy(st);
    }

    public void setStrategy(WeightingStrategy strategy) {
        this.strategy = strategy;
    }

    public WeightingStrategy getStrategy() {
        return strategy;
    }
    
    public void generateOutput(String dir) {
        this.strategy.generateOutput(dir);
    }
}
