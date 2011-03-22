package at.tuwien.ir.textindexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.utils.ConfigUtils;

public class App {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    
    public static void main(String[] args) {
        if (args.length > 0) {
            App.LOGGER.info("Starting application with specific configuration");
            ConfigUtils.loadConfig(args[0]);
        } else {
            App.LOGGER.info("Starting application with default configuration");
            ConfigUtils.loadDefaultConfig();
        }
    }
}
