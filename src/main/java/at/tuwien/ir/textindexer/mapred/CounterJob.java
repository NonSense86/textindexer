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
import at.tuwien.ir.textindexer.utils.IndexCount;

public class CounterJob {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterJob.class);

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

        List<Path> p = new ArrayList<Path>();
        this.addPaths(new File(dir), p);
        Path[] paths = new Path[p.size()];
        for (int i = 0; i < p.size(); i++) {
            //System.out.println(p.get(i).getName());
            paths[i] = p.get(i);
        }

        FileInputFormat.setInputPaths(conf, paths);
        FileOutputFormat.setOutputPath(conf, new Path(Constants.TMP_OUTPUT_PATH));

        try {
            RunningJob job = JobClient.runJob(conf);
            while (!job.isComplete()) {
                System.out.println(job.mapProgress());
                System.out.println(job.reduceProgress());
                
                Thread.sleep(5000);
            }
        } catch (IOException e) {
            LOGGER.error("An error occurred: {}", e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage());
        }
    }
    
    private void addPaths(File dir, List<Path> paths) {
        if (dir.exists()) {
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
