package standard_works;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Text that we are performing analysis on.
 */
public class Text {

    private String title;
    private String fullText;
    private TextType type;
    private NGramCollection nGramCollection;

    private static Set<String> allWords;

    public Text(String _title, String _fullText, TextType _type) {
        title = _title;
        type = _type;
        fullText = _fullText;

        nGramCollection = new NGramCollection();
    }

    /**
     * Extracts all of the NGrams from the full text.
     */
    public void extractNGrams() {
        if (allWords == null) {
            getWords();
        }

        Document document = new Document(fullText);

        for (Sentence sentence : document.sentences()) {
            String[] s = removeNonWords(toLowerCase(sentence.words().toArray(new String[]{})));

            for (int n = 1; n <= 5; n++) {
                for (NGram g : NGram.extractFromSentence(s, n)) {
                    nGramCollection.addNGram(g);
                }
            }
        }
    }

    public NGramCollection getNGramCollection(){
        return nGramCollection;
    }

    /**
     * Helper method. Takes words and turns them to lower case.
     *
     * @param words - collection of words
     * @return lower case collection of words
     */
    private String[] toLowerCase(String[] words) {
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toLowerCase();
        }

        return words;
    }

    /**
     * Helper method. Removes non-words from the collection of strings provided.
     *
     * TODO: This could filter too much out. Consider removing.
     *
     * @param strings to filter
     * @return Array of words
     */
    private String[] removeNonWords(String[] strings) {
        List<String> words = new ArrayList<String>();

        for (int i = 0; i < strings.length; i++) {
            if (allWords.contains(strings[i])) {
                words.add(strings[i]);
            }
        }

        return words.toArray(new String[]{});
    }

    /**
     * Helper method.
     */
    private static void getWords() {
        allWords = new HashSet<String>();

        try {
            Scanner s = new Scanner(new File("/usr/share/dict/american-english"));

            while(s.hasNextLine()) {
                allWords.add(s.nextLine().toLowerCase().trim());
            }

            s.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find american english dictionary file. You must not be on linux.");
        }
    }

    public String getTitle() {
        return title;
    }

    public TextType getType() {
        return type;
    }

    public String getFileName() {
        return title.toLowerCase().replaceAll(" ", "_");
    }
}
