package at.tuwien.ir.textindexer.filtering;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.utils.ConfigUtils;
import at.tuwien.ir.textindexer.utils.IndexCount;
import at.tuwien.ir.textindexer.utils.IndexOutputCollector;

public class ThresholdFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThresholdFilter.class);

    public void filter() {
        float low = this.initLowFrequency();
        float high = this.initHighFrequency();

        if (low > high) {
            LOGGER.error("low frequency threshold is higher than the high frequency threshold, setting to 0 and 100");
            low = 0f;
            high = 100f;
        }
        
        Map<String, IndexCount> map = IndexOutputCollector.getInstance().getOutputMap();
        int documents = IndexOutputCollector.getInstance().getInputFiles().size();
        
        Iterator<String> words = map.keySet().iterator();
        LOGGER.info("Features before filtering: {}", map.keySet().size());
        while(words.hasNext()) {
            String word = words.next();
            int df = map.get(word).getDocFrequency();
            
            float freq = (float) df / documents * 100f;
            
            if (freq < low || freq > high) {
                map.remove(word);
            }
        }
        LOGGER.info("Features after filtering: {}", map.keySet().size());

    }

    private float initLowFrequency() {
        final String lprop = ConfigUtils.getProperty(Constants.LOW_FREQ_THRESHOLD);
        float low;

        if (lprop == null) {
            low = 0f;
        } else {
            try {
                low = Float.parseFloat(lprop);
                if (low < 0f || low >= 100f) {
                    LOGGER.warn("Low threshold frequency[{}] is not valid, setting to 0", low);
                    low = 0f;
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("Low threshold frequency [{}] is not valid, setting to 0", lprop);
                low = 0f;
            }
        }

        return low;
    }

    private float initHighFrequency() {
        final String hprop = ConfigUtils.getProperty(Constants.HIGH_FREQ_THRESHOLD);
        float high;

        if (hprop == null) {
            high = 100f;
        } else {
            try {
                high = Float.parseFloat(hprop);
                if (high < 0f || high > 100f) {
                    LOGGER.warn("High threshold frequency[{}] is not valid, setting to 100", high);
                    high = 100f;
                }
            } catch (NumberFormatException e) {
                LOGGER.warn("High threshold frequency [{}] is not valid, setting to 100", hprop);
                high = 1000f;
            }
        }

        return high;
    }
}
