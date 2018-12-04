package standard_works;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Main class where the magic happens.
 */
public class Main {

    private static final String[] MORMON_TEXTS = {};
    private static final String[] NON_MORMON_TEXTS = { "kjv.txt" /*, "the_late_war.txt", "view_of_the_hebrews.txt" */ };

    public static void main(String[] args) {
        List<Text> texts = new ArrayList();

        for(int i = 0; i < NON_MORMON_TEXTS.length; i++) {
            File f = new File("texts/non_mormon_texts/" + NON_MORMON_TEXTS[i]);
            Text t = new Text(NON_MORMON_TEXTS[i], extractFullText(f), TextType.NON_MORMON);
            t.extractNGrams();

            for (NGram n : t.getNGramCollection().getNGramSet(2)) {
                System.out.println(n + "\t" + t.getNGramCollection().frequencyOf(n));
            }
//            texts.add(t);
        }
    }

    /**
     * Helper method. Returns the full text of the file provided.
     *
     * @param f - File to extract from
     * @return String full text
     */
    private static String extractFullText(File f) {
        StringBuilder builder = new StringBuilder();
        Pattern p = Pattern.compile("\\d+:\\d+");

        try {
            Scanner s = new Scanner(f);

            while(s.hasNextLine()) {
                String line = s.nextLine();
                line = p.matcher(line).replaceAll("");
                builder.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file!");
        }

        return builder.toString();
    }
}
