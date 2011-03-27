package at.tuwien.ir.textindexer.mapred;

import java.io.File;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;

public abstract class MapRedJob implements Runnable {

    public abstract JobConf getJob(String dir);
    
    /**
     * Scans the given directory recursively and adds new {@link Path} objects
     * to the provided list. This list is used afterwards for the job
     * configuration.
     * 
     * @param dir
     * @param paths
     */
    protected void addPaths(File dir, List<Path> paths) {
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
