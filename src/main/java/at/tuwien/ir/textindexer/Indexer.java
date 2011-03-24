package at.tuwien.ir.textindexer;

import at.tuwien.ir.textindexer.mapred.CounterJob;
import at.tuwien.ir.textindexer.stemming.Stemmer;
import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

/**
 * Starts a map reduce job for counting the features.
 * @author petar
 *
 */
public class Indexer {

    /**
     * The MapReduce job.
     */
    private CounterJob countJob;
    
    /**
     * Whether to stem or not.
     */
    private boolean stem;
    
    /**
     * The stemmer used while stemming. It is an
     * implementation of the Porter Stemmer.
     */
    private Stemmer stemmer;
    
    /**
     * The default constructor initializes the stemmer
     * and the mapreduce job. Stemming is off per default.
     */
    public Indexer() {
        this.stemmer = new Stemmer();
        this.countJob = new CounterJob();
        this.stem = false;
    }
    
    /**
     * Initializes the Stemmer and the mapreduce job and
     * sets the stemming on or off.
     * @param stem whether to stem or not.
     */
    public Indexer(boolean stem) {
        this();
        this.stem = stem;
    }

    /**
     * Starts the map reduce job on the specified dir.
     * @param dir
     */
    public void start(String dir) {
        if (this.isStem()) {
            //TODO stem ... this shall be propagated to the map reduce job;
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
