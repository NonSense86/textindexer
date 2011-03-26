package at.tuwien.ir.textindexer.mapred;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.Path;
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
        JobConf conf = new JobConf(CounterJob.class);
        conf.setJobName("wordcount");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IndexCount.class);

        conf.setMapperClass(IndexMapper.class);
        conf.setCombinerClass(IndexReducer.class);
        conf.setReducerClass(IndexReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        conf.set(Constants.STEMMING, ConfigUtils.getProperty(Constants.STEMMING));
        
        List<Path> p = new ArrayList<Path>();
        this.addPaths(new File(dir), p);
        Path[] paths = new Path[p.size()];
        for (int i = 0; i < p.size(); i++) {
            paths[i] = p.get(i);
        }

        if (paths.length > 0) {

            FileInputFormat.setInputPaths(conf, paths);
            FileOutputFormat.setOutputPath(conf, new Path(Constants.TMP_OUTPUT_PATH));

            try {
                RunningJob job = JobClient.runJob(conf);

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
