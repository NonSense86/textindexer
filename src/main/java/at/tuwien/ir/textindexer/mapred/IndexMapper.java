package at.tuwien.ir.textindexer.mapred;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;
import at.tuwien.ir.textindexer.utils.Utilities;

/**
 * The Mapper maps words to {@link IndexCount} objects as the intermediate
 * result.
 * 
 * @author petar
 * 
 */
public class IndexMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IndexCount> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexMapper.class);

    private Text word = new Text();

    private String inputFile;
    
    private boolean stem;

    // it is called by the framework everytime the file changes....
    public void configure(JobConf conf) {
        this.stem = new Boolean(conf.get(Constants.STEMMING));
        File f = new File(conf.get("map.input.file"));
        
        this.inputFile = f.getParentFile().getName() + File.separator + f.getName();
        IndexOutputCollector.getInstance().getInputFiles().put(this.inputFile, null);
    }

    public void map(LongWritable key, Text value, OutputCollector<Text, IndexCount> output, Reporter reporter)
            throws IOException {

        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens()) {
            String token = Utilities.preprocess(tokenizer.nextToken().toLowerCase(), this.stem);

            // after removing punctuation it may be that a token becomes two or
            // more tokens
            // so further splitting is necessary...
            // if not just proceed...
            // Punctuatuon is already removed and the string shall be stemmed (if setup)
            // so no further preprocessing is needed.
            if (token.contains(" ")) {
                StringTokenizer tknzr = new StringTokenizer(token);
                while (tknzr.hasMoreTokens()) {
                    String tkn = tknzr.nextToken();
                    this.collect(tkn, output);
                }
            } else {
                this.collect(token, output);
            }

        }

    }

    private void collect(String token, OutputCollector<Text, IndexCount> output) throws IOException {
        if (!Utilities.isStopWord(token)) {
            word.set(token);
            IndexCount ic = new IndexCount();
            ic.incrTermFrequency(this.inputFile, 1);
            output.collect(word, ic);
        }
    }

}
