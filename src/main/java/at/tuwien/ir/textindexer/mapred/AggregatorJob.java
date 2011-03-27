package at.tuwien.ir.textindexer.mapred;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;
import at.tuwien.ir.textindexer.utils.Utilities;
import at.tuwien.ir.textindexer.weighting.BooleanWeightingStrategy;

/**
 * The Map/Reduce counting job.
 * 
 * @author petar
 * 
 */
public class AggregatorJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorJob.class);

    /**
     * Configures the job and runs it.
     * 
     * @param dir
     */
    public void doJob(String dir) {
        MapRedJob index = new IndexingJob(dir);
        MapRedJob counting = new WordCountingJob(dir);

        try {
            Thread i = new Thread(index);
            Thread c = new Thread(counting);
            i.start();
            c.start();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
