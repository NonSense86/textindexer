package at.tuwien.ir.textindexer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tools.ant.filters.StringInputStream;

import at.tuwien.ir.textindexer.common.Constants;
import at.tuwien.ir.textindexer.stemming.Stemmer;

/**
 * Offers some utility methods to help other tasks.
 * 
 * @author petar
 * 
 */
public final class Utilities {

    /**
     * Moves the output of the current map reduce task to a specified output
     * folder. This allows a subsequent map reduce procedure without deleting
     * the results of the previous.
     */
    public static void mergeOutput(String suffix) {
        final File tmp = new File(Constants.TMP_OUTPUT_PATH + suffix);
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
                    f.renameTo(new File(finalOutputDir, "results_" + suffix + "_" + df.format(new Date()) + ".txt"));
                }
            }

            tmp.delete();
        }
    }

    /**
     * Checks if a word is a stop word.
     * 
     * @param word
     * @return
     */
    public static boolean isStopWord(String word) {
        return Constants.STOP_WORDS.contains(word);
    }

    /**
     * Removes punctuation, trailing and heading spaces and stems if the flag
     * stem is set to true.
     * 
     * @param word
     * @param stem
     * @return
     */
    public static String preprocess(String word, boolean stem) {
        String result = Utilities.removePunctuation(word);

        if (stem) {
            result = Utilities.stem(result);
        }

        return result;
    }

    /**
     * Removes certain characters and trailing and heading spaces of words.
     * 
     * @param word
     * @return
     */
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

    private static String stem(String word) {
        final Stemmer pStemmer = new Stemmer();

        for (int i = 0; i < word.length(); i++) {
            pStemmer.add(word.charAt(i));
        }

        pStemmer.stem();
        return pStemmer.toString();

    }

    private Utilities() {

    }
}
