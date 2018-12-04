package standard_works;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collection of NGrams to be associated with a text.
 */
public class NGramCollection {

    private Map<Integer, Set<NGram>> nGrams; // maps size to set of NGrams
    private Map<NGram, Integer> frequencies; // maps individual nGram to frequency

    public NGramCollection() {
        nGrams = new HashMap<Integer, Set<NGram>>();
        frequencies = new HashMap<NGram, Integer>();
    }

    /**
     * Returns set of NGrams for size n.
     *
     * @param n - size of NGrams
     * @return Set of NGrams
     */
    public Set<NGram> getNGramSet(int n) {
        return nGrams.get(n);
    }

    /**
     * Returns the frequency of the nGram provided.
     *
     * @param nGram to get frequency
     * @return int
     */
    public int frequencyOf(NGram nGram) {
        return frequencies.get(nGram);
    }

    /**
     * Add an NGram to the collection.
     *
     * @param nGram to add
     */
    public void addNGram(NGram nGram) {
        if (!nGrams.containsKey(nGram.size())) {
            nGrams.put(nGram.size(), new HashSet<NGram>());
        }

        nGrams.get(nGram.size()).add(nGram);

        if (frequencies.containsKey(nGram)) {
            frequencies.put(nGram, frequencies.get(nGram) + 1);
        } else {
            frequencies.put(nGram, 1);
        }
    }

    public String nGramToCSV() {
        throw new RuntimeException("Implement this");
    }

    public String frequencyToCSV() {
        throw new RuntimeException("Implement this");
    }

    public Set<Map.Entry<NGram, Integer>> getFrequencies() {
        return frequencies.entrySet();
    }
}
