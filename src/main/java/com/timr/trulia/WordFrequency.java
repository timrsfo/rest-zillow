package com.timr.trulia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
     * @throws FileNotFoundException,
     *             IOException
     */
    public Map<String, Integer> getFileWordCount(File file) throws FileNotFoundException, IOException {

        Map<String, Integer>map = null;
        if (file == null) {
            throw new IllegalArgumentException("file: " + file);
        } else {
            map = new HashMap<String, Integer>();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                getLineWordCount(map, line);
            }
        }
        return map;
    }

    /**
     * Get a list of all words with count occurrences 
     * @param count
     * @return
     */
    List<String> getWordsOfCount(Map<String, Integer>map, int count){
        if(map == null || count <= 0){
            throw new IllegalArgumentException();
        }
        Map<Integer, List<String>> countMap = new HashMap<Integer, List<String>>();
        List<String> list = null;
        for(Entry<String, Integer> entry : map.entrySet()) {
            if(countMap.containsKey(entry.getValue())){
                list = countMap.get(entry.getValue());
                list.add(entry.getKey());
            } else {
              list = new ArrayList<String>();  
              list.add(entry.getKey());
              countMap.put(entry.getValue(), list);
            }
        }
        list = countMap.get(count);
        if(list == null)
            return null;
        Collections.sort(list);
        return list;
    }

}