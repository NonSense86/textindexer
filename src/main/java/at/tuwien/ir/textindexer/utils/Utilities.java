package at.tuwien.ir.textindexer.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.tuwien.ir.textindexer.common.Constants;

public final class Utilities {

    public static void mergeOutput() {
        final File tmp = new File(Constants.TMP_OUTPUT_PATH);
        final File finalOutputDir = new File(ConfigUtils.getProperty(Constants.OUTPUT_PATH));
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");

        if (!finalOutputDir.exists()) {
            finalOutputDir.mkdirs();
        }

        if (tmp.exists() && tmp.isDirectory()) {
            for (File f : tmp.listFiles()) {
                if (f.getName().startsWith(".")) {
                    f.delete();
                } else {
                    f.renameTo(new File(finalOutputDir, "results_" + df.format(new Date())));
                }
            }

            tmp.delete();
        }
    }
    
    public static boolean isStopWord(String word) {
        return Constants.STOP_WORDS.contains(word);
    }
    
    public static String removePunctuation(String word) {
        String result = "";
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Constants.PUNCTUATION.indexOf(c) == -1) {
                result += c;
            } else {
                result += " ";
            }
        }
    
        return removeTrailingSpaces(removeHeadingSpaces(result));
    }
    
    private static String removeTrailingSpaces(String word) {
        while (word.endsWith(" ")) {
            word = word.substring(0, word.lastIndexOf(" "));
        }
        
        return word;
    }
    
    private static String removeHeadingSpaces(String word) {
        while (word.startsWith(" ")) {
            word = word.substring(1);
        }
        
        return word;
    }

    private Utilities() {

    }
}
