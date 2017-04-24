package com.timr.trulia;

import static com.timr.utils.hamcrest.IsMapWithSize.isMapWithSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WordFrequencyTest {
    static final Logger logger = Logger.getLogger(WordFrequencyTest.class);
    static final int TRULIA_MAP_SIZE = 32; // dolor(2), quam(2), vulputate(2)

    private WordFrequency wordFreq = null;

    @BeforeTest
    public void beforeTest() {
        wordFreq = new WordFrequency();
    }

    @AfterTest
    public void afterTest() {

    }
 
    @DataProvider
    public Object[][] wordLines() {
        return new Object[][] {
                // line, ck-word, ck-count, numEntries
                { "one", "one", 1, 1 }, { "one. ", "one", 1, 1 }, { "one one. ", "one", 2, 1 },
                { "?., ... : *   - # one one. ", "one", 2, 1 }, { "one one two", "two", 1, 2 }, };
    }

    @Test(dataProvider = "wordLines")
    public void testWordCountLine(String line, String word, int count, int size) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        wordFreq.getLineWordCount(map, line);
        assertThat(map, hasEntry(word, count));
        assertThat(map, isMapWithSize(size));
    }

    @Test
    public void testOneWordMultipleLinesEntryCountFile() throws FileNotFoundException, IOException {
        Map<String, Integer> actual = null;
        actual = wordFreq.getFileWordCount(new File("src/test/resources/OneWordMultipleLines.txt"));
        assertThat(actual, isMapWithSize(1));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullFile() throws IOException{
            wordFreq.getFileWordCount(null);
    }

    @Test(expectedExceptions = FileNotFoundException.class)
    public void testFileNotExist() throws FileNotFoundException, IOException{
        wordFreq.getFileWordCount(new File("src/bad/path"));
    }

    @Test(expectedExceptions = FileNotFoundException.class)
    public void testIsNotAFile() throws FileNotFoundException, IOException{
        wordFreq.getFileWordCount(new File("src/test"));
    }


    @Test
    public void testOneWordMultipleLinesVerifyEntryFile() {
        Map<String, Integer> actual = null;
        try {
            actual = wordFreq.getFileWordCount(new File("src/test/resources/OneWordMultipleLines.txt"));
        } catch (FileNotFoundException e) {
            Assert.assertTrue(false, e.getMessage());
        } catch (IOException ioe) {
            Assert.assertTrue(false, ioe.getMessage());
        }
        assertThat(actual, hasEntry("one", 7));
    }

    @Test
    public void testTuliaTestFile() throws FileNotFoundException, IOException {
        Map<String, Integer> actual = null;
            actual = wordFreq.getFileWordCount(new File("src/test/resources/trulia-sample.txt"));
        assertThat(actual, hasEntry("dolor", 2));
        assertThat(actual, hasEntry("quam", 3));
        assertThat(actual, hasEntry("vulputate", 2));
        assertThat(actual, isMapWithSize(TRULIA_MAP_SIZE));
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNoMapForCount(){
        wordFreq.getWordsOfCount(null, 1);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadCount() throws FileNotFoundException, IOException {
        Map<String, Integer> actual = new HashMap<>();
        wordFreq.getWordsOfCount(actual, 0);
    }
    
    @DataProvider
    public Object[][] wordCounts() {
        return new Object[][] {
                // word, count, filename
                { "quam", 3, "trulia-sample.txt"}, 
                };
    }

    @Test(dataProvider = "wordCounts")
    public void testWordsOfCount(String word, int count, String fileName) throws FileNotFoundException, IOException {
        String testFilePath = "src/test/resources/";
        Map<String, Integer> actual = null;
        actual = wordFreq.getFileWordCount(new File(testFilePath + fileName));
        List<String> words = wordFreq.getWordsOfCount(actual, count);
        assertThat(words.get(0), equalTo(word));
    }
    @Test
    public void testWordsOfCountMultiMatch() throws FileNotFoundException, IOException {
        String testFilePath = "src/test/resources/";
        String fileName = "trulia-sample.txt";
        Map<String, Integer> map = null;
        map = wordFreq.getFileWordCount(new File(testFilePath + fileName));
        List<String> actual = wordFreq.getWordsOfCount(map, 2);
        assertThat(actual, containsInAnyOrder("vulputate", "dolor"));

    }



}
