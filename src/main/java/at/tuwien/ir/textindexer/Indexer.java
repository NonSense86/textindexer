package at.tuwien.ir.textindexer;

import at.tuwien.ir.textindexer.mapred.AggregatorJob;
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
    private AggregatorJob countJob;
    
    
    /**
     * The default constructor initializes the stemmer
     * and the mapreduce job. Stemming is off per default.
     */
    public Indexer() {
        this.countJob = new AggregatorJob();
    }
    

    /**
     * Starts the map reduce job on the specified dir.
     * @param dir
     */
    public void start(String dir) {
        this.countJob.doJob(dir);
    }

}
