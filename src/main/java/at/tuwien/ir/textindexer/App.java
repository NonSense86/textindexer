package at.tuwien.ir.textindexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.Utilities;
import at.tuwien.ir.textindexer.weighting.BooleanWeightingStrategy;

public class App {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        String dir = null;
        if (args.length > 1) {
            App.LOGGER.info("Starting application with specific configuration");
            ConfigUtils.loadConfig(args[0]);
            dir = args[1];
        } else if (args.length == 1){
            App.LOGGER.info("Starting application with default configuration");
            ConfigUtils.loadDefaultConfig();
            dir = args[0];
        } else {
            App.LOGGER.error("No arguments provided. Please provide at least one input folder.");
            System.exit(1);
        }
        
        Indexer idx = new Indexer(new BooleanWeightingStrategy(), false);
        idx.start(dir);
        Utilities.mergeOutput();
    }
}
