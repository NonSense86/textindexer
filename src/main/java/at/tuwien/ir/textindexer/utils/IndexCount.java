package at.tuwien.ir.textindexer.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * A helper object for the values in the intermediate Output of the map reduce procedure.
 * It does some friggin serializing because of hadoop.
 * @author petar
 *
 */
public class IndexCount implements Writable {


    /**
     * The key is the name of the document The value is the number of times a
     * word is associated with this document.
     */
    private Map<Text, IntWritable> termFrequency;

    public IndexCount() {
        this.setTermFrequency(new HashMap<Text, IntWritable>());
    }

    public void incrTermFrequency(String doc, int incr) {
        Text t = new Text(doc);
        IntWritable tf = this.termFrequency.get(t);

        if (tf == null) {
            this.termFrequency.put(t, new IntWritable(incr));
        } else {
            this.termFrequency.put(t, new IntWritable(tf.get() + incr));
        }
    }

    public int getDocFrequency() {
        return this.termFrequency.keySet().size();
    }

    public void setTermFrequency(Map<Text, IntWritable> termFrequency) {
        this.termFrequency = termFrequency;
    }

    public Map<Text, IntWritable> getTermFrequency() {
        return termFrequency;
    }

    public void readFields(DataInput in) throws IOException {
        this.termFrequency = new HashMap<Text, IntWritable>();

        int numEntries = in.readInt();
            
        String keyClassName = in.readUTF();
        String valueClassName = in.readUTF();

        Text objK;
        IntWritable objV;
        try {
            Class keyClass = Class.forName(keyClassName);
            Class valueClass = Class.forName(valueClassName);
            for (int i = 0; i < numEntries; i++) {
                objK = (Text) keyClass.newInstance();
                objK.readFields(in);
                objV = (IntWritable) valueClass.newInstance();
                objV.readFields(in);
                this.termFrequency.put(objK, objV);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(this.termFrequency.size());
        if (this.termFrequency.size() == 0) {
            return;
        }

        // Write out the class names for keys and values assuming that all
        // entries have the same type.
        Set<Entry<Text, IntWritable>> entries = this.termFrequency.entrySet();
        Map.Entry<Text, IntWritable> first = entries.iterator().next();
        Text objK = first.getKey();
        IntWritable objV = first.getValue();
        out.writeUTF(objK.getClass().getCanonicalName());
        out.writeUTF(objV.getClass().getCanonicalName());

        // Then write out each key/value pair.
        for (Map.Entry<Text, IntWritable> e : this.termFrequency.entrySet()) {
            e.getKey().write(out);
            e.getValue().write(out);
        }

    }

    public String toString() {
        return this.getDocFrequency() + " " + this.termFrequency.toString();
    }

}
