package at.tuwien.ir.textindexer.utils;

import java.io.File;

import org.junit.Test;

import at.tuwien.ir.textindexer.common.Constants;

import junit.framework.Assert;

public class ConfigUtilsTest {
    
    @Test
    public void shallLoadDefaultConfig() throws Exception {
        String noConf = null;
        ConfigUtils.loadConfig(noConf);
        String weighting = ConfigUtils.getProperty(Constants.WEIGHTING);
        String stemming = ConfigUtils.getProperty(Constants.STEMMING);
        Assert.assertEquals("false", stemming);
        Assert.assertEquals(false, new Boolean(stemming).booleanValue());
        Assert.assertEquals("bool", weighting);
        
    }
    
    @Test
    public void shallLoadSpecificConfig() throws Exception {
        ConfigUtils.loadConfig(new File("src/test/resources/test_config.properties"));
        String weighting = ConfigUtils.getProperty(Constants.WEIGHTING);
        String stemming = ConfigUtils.getProperty(Constants.STEMMING);
        Assert.assertEquals("true", stemming);
        Assert.assertEquals(true, new Boolean(stemming).booleanValue());
        Assert.assertEquals("tf", weighting);
    }

    @Test
    public void shallGetNonExistingProperty() throws Exception {
        ConfigUtils.loadConfig(new File("src/test/resources/test_config.properties"));
        String freq_high = ConfigUtils.getProperty(Constants.HIGH_FREQ_THRESHOLD);
        Assert.assertNotNull(freq_high);
        Assert.assertEquals(10, Integer.parseInt(freq_high));
    }
    
    @Test
    public void shallRetrieveNullForNonExistingProperty() throws Exception {
        File f = null;
        ConfigUtils.loadConfig(f);
        String nonExisting = ConfigUtils.getProperty("non.exisiting.key");
        Assert.assertNull(nonExisting);
        
    }
}
