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
public class CounterJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterJob.class);
    
    

    /**
     * Configures the job and runs it.
     * 
     * @param dir
     */
    public void doJob(String dir) {
        JobConf indexing = new JobConf(CounterJob.class);
        indexing.setJobName("indexing");
        indexing.setOutputKeyClass(Text.class);
        indexing.setOutputValueClass(IndexCount.class);

        indexing.setMapperClass(IndexMapper.class);
        indexing.setCombinerClass(IndexReducer.class);
        indexing.setReducerClass(IndexReducer.class);

        indexing.setInputFormat(TextInputFormat.class);
        indexing.setOutputFormat(TextOutputFormat.class);
        indexing.set(Constants.STEMMING, ConfigUtils.getProperty(Constants.STEMMING));
        
        JobConf counting = new JobConf(CounterJob.class);
        counting.setJobName("counting");
        counting.setOutputKeyClass(Text.class);
        counting.setOutputValueClass(IntWritable.class);
        
        counting.setMapperClass(WordCountMapper.class);
        counting.setCombinerClass(WordCountReducer.class);
        counting.setReducerClass(WordCountReducer.class);
        
        counting.setInputFormat(TextInputFormat.class);
        counting.setOutputFormat(TextOutputFormat.class);
        
        List<Path> p = new ArrayList<Path>();
        this.addPaths(new File(dir), p);
        Path[] paths = new Path[p.size()];
        for (int i = 0; i < p.size(); i++) {
            paths[i] = p.get(i);
        }

        if (paths.length > 0) {

            FileInputFormat.setInputPaths(indexing, paths);
            FileOutputFormat.setOutputPath(indexing, new Path(Constants.TMP_OUTPUT_PATH));
            FileInputFormat.setInputPaths(counting, paths);
            FileOutputFormat.setOutputPath(counting, new Path(Constants.TMP_OUTPUT_PATH));

            try {
                JobClient.runJob(indexing);
                Utilities.mergeOutput();
                
                JobClient.runJob(counting);
                Utilities.mergeOutput();

            } catch (IOException e) {
                LOGGER.error("An error occurred: {}", e.getMessage());
            }
        } else {
            LOGGER.error("No Documents found in the specified path {}. Program will exit now...", dir);
            System.exit(1);
        }
    }

    /**
     * Scans the given directory recursively and adds new {@link Path} objects
     * to the provided list. This list is used afterwards for the job
     * configuration.
     * 
     * @param dir
     * @param paths
     */
    private void addPaths(File dir, List<Path> paths) {
        if (dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            for (final File f : files) {
                if (f.isDirectory()) {
                    paths.add(new Path(f.getPath()));
                    this.addPaths(f, paths);
                }
            }
        }
    }
}
