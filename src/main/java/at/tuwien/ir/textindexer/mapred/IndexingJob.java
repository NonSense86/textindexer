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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.Utilities;

public class IndexingJob extends MapRedJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexingJob.class);
    
    private static final String SUFFIX = "_indexing";
    
    private String dir;
    
    public IndexingJob(String dir) {
        this.dir = dir;
    }
    
    public void run() {
        try {
            LOGGER.info("Starting indexing map reduce job");
            JobClient.runJob(this.getJob(this.dir));
            Utilities.mergeOutput(SUFFIX);
        } catch (IOException e) {
            LOGGER.error("An Error occurred: {} ", e.getMessage());
        }
    }

    @Override
    public JobConf getJob(String dir) {
        JobConf indexing = new JobConf(AggregatorJob.class);
        indexing.setJobName("indexing");
        indexing.setOutputKeyClass(Text.class);
        indexing.setOutputValueClass(IndexCount.class);

        indexing.setMapperClass(IndexMapper.class);
        indexing.setCombinerClass(IndexReducer.class);
        indexing.setReducerClass(IndexReducer.class);

        indexing.setInputFormat(TextInputFormat.class);
        indexing.setOutputFormat(TextOutputFormat.class);
        indexing.set(Constants.STEMMING, ConfigUtils.getProperty(Constants.STEMMING));
       
        List<Path> p = new ArrayList<Path>();
        this.addPaths(new File(dir), p);
        Path[] paths = new Path[p.size()];
        for (int i = 0; i < p.size(); i++) {
            paths[i] = p.get(i);
        }

        if (paths.length > 0) {

            FileInputFormat.setInputPaths(indexing, paths);
            FileOutputFormat.setOutputPath(indexing, new Path(Constants.TMP_OUTPUT_PATH + SUFFIX));

            return indexing;
        } else {
            LOGGER.error("No Documents found in the specified path {}.", dir);
            return null;
        }
        
    }

}
