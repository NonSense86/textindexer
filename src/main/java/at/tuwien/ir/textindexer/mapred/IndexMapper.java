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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.Utilities;

public class IndexMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IndexCount> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexMapper.class);

    private static final IntWritable one = new IntWritable(1);

    private Text word = new Text();

    private String inputFile;

    public void configure(JobConf conf) {
        File f = new File(conf.get("map.input.file"));
        this.inputFile = f.getParentFile().getName() + File.separator + f.getName();
    }

    public void map(LongWritable key, Text value, OutputCollector<Text, IndexCount> output, Reporter reporter)
            throws IOException {

        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens()) {
            String token = Utilities.removePunctuation(tokenizer.nextToken().toLowerCase());

            if (!Utilities.isStopWord(token)) {
                word.set(token);
                IndexCount ic = new IndexCount();
                ic.incrDocFrequency(1);
                ic.incrTermFrequency(new Text(this.inputFile), one);
                output.collect(word, ic);
            }

        }

    }

}
