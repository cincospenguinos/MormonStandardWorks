package standard_works;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NGramTest {

    @Test
    public void extractFromSentence() {
        String[] sentence = "this is a sentence".split(" ");

        // Unigrams
        List<NGram> trueUnigrams = new ArrayList<NGram>();
        for (int i = 0; i < sentence.length; i++) {
            trueUnigrams.add(new NGram(sentence[i]));
        }

        List<NGram> unigrams = NGram.extractFromSentence(sentence, 1);
        assertEquivalentLists(trueUnigrams, unigrams);

        // Bigrams
        List<NGram> trueBigrams = new ArrayList<NGram>();
        for (int i = 0; i < sentence.length - 1; i++) {
            trueBigrams.add(new NGram(sentence[i], sentence[i + 1]));
        }

        List<NGram> bigrams = NGram.extractFromSentence(sentence, 2);
        assertEquivalentLists(trueBigrams, bigrams);
    }

    private void assertEquivalentLists(List<NGram> list1, List<NGram> list2) {
        assertEquals("Lists should be equivalent", list1.size(), list2.size());

        for (int i = 0; i < list1.size(); i++) {
            assertEquals("These two elements should be equal", list1.get(i), list2.get(i));
        }
    }
}