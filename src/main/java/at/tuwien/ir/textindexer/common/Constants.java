package at.tuwien.ir.textindexer.common;

import java.util.Arrays;
import java.util.List;

public final class Constants {

    private static final String[] STOP_WORDS_ARRAY = {"", "a", "about", "above", "across", "after", "again", "because", "become", "becomes", "been", "before", "he", "her", "here", "herself", "high", "higher", "through", "thus", "to", "yet", "you", "young", "younger", "youngest", "your", "yours"};
    
    public static List<String> STOP_WORDS;
    
    static {
        STOP_WORDS = Arrays.asList(STOP_WORDS_ARRAY);
    }
    
    public static final String PUNCTUATION = "\t\n\r\f,.<>/?;:'\"[] {}\\|`~!@#$%^&*()_+-=0123456789";
    // \t\n\r\f,:#[]*/\".-?!0123456789@$%()=;'
    
    public static final String WEIGHTING = "at.tuwien.ir.textindexer.weighting";
    
    public static final String LOW_FREQ_THRESHOLD = "at.tuwien.ir.textindexer.frequency.threshold.low";
    
    public static final String HIGH_FREQ_THRESHOLD = "at.tuwien.ir.textindexer.frequency.threshold.high";
    
    public static final String STEMMING = "at.tuwien.ir.textindexer.stemming";
    
    public static final String OUTPUT_PATH = "at.tuwien.ir.textindexer.output.path";
    
    public static final String TMP_OUTPUT_PATH = "./results";
    
    private Constants() {
        
    }
    
}
