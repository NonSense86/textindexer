package at.tuwien.ir.textindexer;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.filtering.ThresholdFilter;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.Utilities;
import at.tuwien.ir.textindexer.weighting.BooleanWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.InverseDocumentFrequencyWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.TermFrequencyWeightingStrategy;
import at.tuwien.ir.textindexer.weighting.WeightingStrategy;

/**
 * Application Main Class.
 * 
 * @author petar
 * 
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * Entry point for the program. Based on the input it loads the specified
     * config or the default config and starts indexing the documents in the
     * specified root folder.
     * 
     * @param args
     */
    public static void main(String[] args) {
        App.LOGGER.info("Starting at: {}", new Date());

        String dir = null;
        if (args.length > 1) {
            App.LOGGER.info("Starting application with specific configuration: {}", args[0]);
            ConfigUtils.loadConfig(args[0]);
            dir = args[1];
        } else if (args.length == 1) {
            App.LOGGER.info("Starting application with default configuration");
            ConfigUtils.loadDefaultConfig();
            dir = args[0];
        } else {
            App.LOGGER.error("No arguments provided. Please provide at least one input folder.");
            App.LOGGER.error("Usage: java -jar bowindexer.jar [config] [dir], "
                    + "where config is the path to the config.properties "
                    + "file and dir is the root folder of the documents.");
            System.exit(1);
        }

        Indexer idx = new Indexer();
        idx.start(dir);
        
        ThresholdFilter tf = new ThresholdFilter();
        tf.filter();
        
        // TODO filter and calculate weight...
        // In the end there has to be one file with all features
        // and two files with filtered features.
        // the filtering is based on the predefined thresholds...
        String weightingType = ConfigUtils.getProperty(Constants.WEIGHTING);

        WeightingStrategy ws;
        if (weightingType.equals("tf")) {
            ws = new TermFrequencyWeightingStrategy();
            LOGGER.info("Selecting [{}] weighting strategy", "Term Frequency");
        } else if (weightingType.equals("idf")) {
            ws = new InverseDocumentFrequencyWeightingStrategy();
            LOGGER.info("Selecting [{}] weighting strategy", "Inverse Document Frequency");
        } else {
            // no matter what else is set.. load the default...
            LOGGER.info("Selecting [{}] weighting strategy", "Boolean");
            ws = new BooleanWeightingStrategy();
        }

        OutputGenerator generator = new OutputGenerator(ws);
        try {
			generator.generateOutput(ConfigUtils.getProperty(Constants.OUTPUT_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        App.LOGGER.info("Terminating at: {}", new Date());
    }
}
