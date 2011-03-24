package at.tuwien.ir.textindexer.mapred;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.utils.IndexCount;

public class IndexReducer extends MapReduceBase implements Reducer<Text, IndexCount, Text, IndexCount> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexReducer.class);

    public void reduce(Text key, Iterator<IndexCount> values, OutputCollector<Text, IndexCount> output,
            Reporter reporter) throws IOException {

        int df = 0;
        IndexCount result = new IndexCount();

        while (values.hasNext()) {
            IndexCount c = values.next();
            df += c.getDocFrequency();
            for (Text doc : c.getTermFrequency().keySet()) {
                result.incrTermFrequency(doc, c.getTermFrequency().get(doc));
            }
        }

        result.incrDocFrequency(df);
        output.collect(key, result);

    }
}
