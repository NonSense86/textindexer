package at.tuwien.ir.textindexer.mapred;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class WordCountMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    
    public void configure(JobConf conf) {
        File f = new File(conf.get("map.input.file"));
        String name = f.getParentFile().getName() + File.separator + f.getName();
        word.set(name);
        IndexOutputCollector.getInstance().getInputFiles().put(name, null);
    }
    
    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
            throws IOException {
        
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while (tokenizer.hasMoreTokens()) {
            tokenizer.nextToken();
            output.collect(word, one);
        }

    }
}
