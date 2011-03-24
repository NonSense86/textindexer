package at.tuwien.ir.textindexer;

import at.tuwien.ir.textindexer.mapred.CounterJob;
import at.tuwien.ir.textindexer.stemming.Stemmer;
import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

public class Indexer {

    private CounterJob countJob;
    
    private boolean stem;
    
    private Stemmer stemmer;
    
    public Indexer() {
        this.stemmer = new Stemmer();
        this.countJob = new CounterJob();
        this.stem = false;
    }
    
    public Indexer(boolean stem) {
        this();
        this.stem = stem;
    }

    public void start(String dir) {
        if (this.isStem()) {
            //TODO stem;
        }
        this.countJob.doJob(dir);
    }

    public void setStem(boolean stem) {
        this.stem = stem;
    }

    public boolean isStem() {
        return stem;
    }
}
