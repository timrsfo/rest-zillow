package com.timr.trulia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Frequency of word occurrence in a text file
 * 
 * @author timr
 *
 */
public class WordFrequency {
    static final Logger logger = Logger.getLogger(WordFrequency.class);

    /**
     * Count occurrences of words in a line of text
     * 
     * @param map
     * @param line
     */
    public void getLineWordCount(Map<String, Integer> map, String line) {
        String[] words = line.split("[^A-Za-z0-9]");
        for (int i = 0; i < words.length; i++) {
            if (map.containsKey(words[i])) {
                map.put(words[i], map.get(words[i]) + 1);
            } else {
                map.put(words[i], 1);
            }
        }
        map.remove(""); // @BUGFIX split inserts empty string into array
    }

    /**
     * Count occurrences of words in a file
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public Map<String, Integer> getFileWordCount(File file) throws FileNotFoundException, IOException {

        if (file == null) {
            throw new IllegalArgumentException("file: " + file);
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                getLineWordCount(map, line);
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ioe) {
                // can't do much with this except log it
                logger.error(ioe.getMessage());
            }
        }

        return map;
    }

    /**
     * Count occurrences of words in a file
     * Using Java 7 1. Try with resources 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public Map<String, Integer> getFileWordCountJ7(File file) throws FileNotFoundException, IOException {

        if (file == null) {
            throw new IllegalArgumentException("file: " + file);
        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                getLineWordCount(map, line);
            }
        }
        return map;
    }

}