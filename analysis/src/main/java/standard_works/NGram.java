package standard_works;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing an NGram.
 */
public class NGram {

    private String[] words;

    public NGram(String unigram) {
        words = new String[1];
        words[0] = unigram;
    }

    public NGram(String bigram1, String bigram2) {
        words = new String[2];
        words[0] = bigram1;
        words[1] = bigram2;
    }

    public NGram(String[] _words) {
        words = new String[_words.length];
        System.arraycopy(_words, 0, words, 0, words.length);
    }

    /**
     * Returns size of this NGram.
     *
     * @return int
     */
    public int size() {
        return words.length;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            builder.append(words[i]);
            builder.append(' ');
        }

        return builder.toString().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NGram) {
            NGram other = (NGram) o;

            if (other.size() == this.size()) {
                for (int i = 0; i < this.words.length; i++) {
                    if (!other.words[i].equals(this.words[i])) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(words);
    }

    /**
     * Static helper method. Extracts n-grams
     *
     * @param sentence - Strings in a sentence
     * @param n - Size of NGrams
     * @return List of NGrams
     */
    public static List<NGram> extractFromSentence(String[] sentence, int n) {
        List<NGram> nGrams = new ArrayList<NGram>();

        for (int i = 0; i < sentence.length - (n - 1); i++) {
            nGrams.add(new NGram(Arrays.copyOfRange(sentence, i, i + n)));
        }

        return nGrams;
    }
}
