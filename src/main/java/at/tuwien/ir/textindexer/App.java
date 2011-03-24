package at.tuwien.ir.textindexer;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.Utilities;
import at.tuwien.ir.textindexer.weighting.BooleanWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.InverseDocumentFrequencyWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.TermFrequencyWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

public class App {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        App.LOGGER.info("Starting at: {}", new Date()); 
        
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
        
        Indexer idx = new Indexer();
        idx.start(dir);
        
        //won't be necessary in the end 
        Utilities.mergeOutput();
        //System.out.println(IndexOutputCollector.getInstance().getOutputMap().size());
        
        
        //TODO filter and calculate weight...
        // In the end there has to be one file with all features
        // and two files with filtered features.
        // the filtering is based on the predefined thresholds...
        String weightingType = ConfigUtils.getProperty(Constants.WEIGHTING);
        
        WeightingStrategy ws;
        if (weightingType.equals("tf")) {
            ws = new TermFrequencyWeightingStrategy();
        } else if (weightingType.equals("idf")) {
            ws = new InverseDocumentFrequencyWeightingStrategy();
        } else {
            //no matter what else is set.. load the default...
            ws = new BooleanWeightingStrategy();
        }
        
        OutputGenerator generator = new OutputGenerator(ws);
        generator.generateOutput(ConfigUtils.getProperty(Constants.OUTPUT_PATH));
        
        App.LOGGER.info("Terminating at: {}", new Date());
    }
}
