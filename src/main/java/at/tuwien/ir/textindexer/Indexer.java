package at.tuwien.ir.textindexer;

import at.tuwien.ir.textindexer.mapred.CounterJob;
import at.tuwien.ir.textindexer.stemming.Stemmer;
import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

public class Indexer {

    private WeightingStrategy strategy;
    
    private CounterJob countJob;
    
    private OutputGenerator outputGenerator;
    
    private boolean stem;
    
    private Stemmer stemmer;
    
    public Indexer() {
        this.stemmer = new Stemmer();
        this.countJob = new CounterJob();
    }
    
    public Indexer(WeightingStrategy strategy) {
        this();
        this.setStrategy(strategy);
    }
    
    public Indexer(WeightingStrategy strategy, boolean stem) {
        this(strategy);
        this.stem = stem;
    }

    public void setStrategy(WeightingStrategy strategy) {
        this.strategy = strategy;
    }

    public WeightingStrategy getStrategy() {
        return strategy;
    }
    
    public void start(String dir) {
        if (this.isStem()) {
            //TODO stem;
        }
        //this.strategy.index(dir);
        this.countJob.doJob(dir);
    }

    public void setStem(boolean stem) {
        this.stem = stem;
    }

    public boolean isStem() {
        return stem;
    }
}
