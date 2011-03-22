package at.tuwien.ir.textindexer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);
    
    private static final String DEFAULT_CONFIG = "default_config.properties";
    
    public static final String WEIGHTING = "at.tuwien.ir.textindexer.weighting";
    
    public static final String LOW_FREQ_THRESHOLD = "at.tuwien.ir.textindexer.frequency.threshold.low";
    
    public static final String HIGH_FREQ_THRESHOLD = "at.tuwien.ir.textindexer.frequency.threshold.high";
    
    public static final String STEMMING = "at.tuwien.ir.textindexer.stemming";
    
    private static Properties defaultConfig = null;
    
    private static Properties config = null;
    
    public static void loadConfig(String config) {
        File conf = null;
        if (config != null) {
            conf = new File(config);
        }
        ConfigUtils.loadConfig(conf);
    }
    
    public static void loadConfig(File config) {
        boolean ok = true;
        if (config == null) {
            ConfigUtils.LOGGER.info("No config file provided, loading the default config");
            ok = false;
        } else if (!config.exists() || !config.isFile()) {
            ConfigUtils.LOGGER.warn(
                "The config file provided either does not exist, or is not a file: {}. Loading default config file.",
                config.getPath());
            ok = false;
        }
        
        ConfigUtils.loadDefaultConfig();
        
        if (ok) {
            try {
                ConfigUtils.config = new Properties();
                final FileInputStream fis = new FileInputStream(config);
                ConfigUtils.config.load(fis);
                fis.close();
                
            } catch (IOException e) {
                LOGGER.warn("Could not load the specified config file {} due to {}. Loading default config", config
                    .getPath(), e.getMessage());
            }
        }
    }
    
    public static String getProperty(final String key) {
        if (ConfigUtils.config != null) {
            String prop = ConfigUtils.config.getProperty(key);
            if (prop != null) {
                return prop;
            }
        }
        
        return ConfigUtils.defaultConfig.getProperty(key);
    }
    
    public static void loadDefaultConfig() {
        try {
            LOGGER.debug("Loading default config file...");
            ConfigUtils.defaultConfig = new Properties();
            final URL url = ClassLoader.getSystemResource(ConfigUtils.DEFAULT_CONFIG);
            ConfigUtils.defaultConfig.load(url.openStream());
            
        } catch (IOException e) {
            LOGGER.error("Could not load the default config file due to {}. System will exit now...", e.getMessage());
            System.exit(1);
        }
    }
    
    private ConfigUtils() {
        
    }
}
