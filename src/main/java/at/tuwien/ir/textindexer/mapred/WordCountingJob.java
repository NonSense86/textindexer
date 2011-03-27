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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;

public class WordCountingJob extends MapRedJob {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountingJob.class);
    
    private String dir;
    
    public WordCountingJob(String dir) {
        this.dir = dir;
    }
    
    public void run() {
        try {
            JobClient.runJob(this.getJob(this.dir));
        } catch (IOException e) {
            LOGGER.error("An Error occurred: {} ", e.getMessage());
        }
    }

    @Override
    public JobConf getJob(String dir) {
        JobConf counting = new JobConf(AggregatorJob.class);
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

            FileInputFormat.setInputPaths(counting, paths);
            FileOutputFormat.setOutputPath(counting, new Path(Constants.TMP_OUTPUT_PATH));

            return counting;
        } else {
            LOGGER.error("No Documents found in the specified path {}.", dir);
            return null;
        }
    }

}
