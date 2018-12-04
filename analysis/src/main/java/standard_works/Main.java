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

    private static final String[] MORMON_TEXTS = { "the_book_of_mormon.txt", "pearl_of_great_price.txt", "d&c.txt" };
    private static final String[] NON_MORMON_TEXTS = { "kjv.txt" , "the_late_war.txt" };

    public static void main(String[] args) {
        List<Text> texts = new ArrayList();

        for (int i = 0; i < MORMON_TEXTS.length; i++) {
            File f = new File("texts/mormon_texts/" + MORMON_TEXTS[i]);
            Text t = new Text(MORMON_TEXTS[i], extractFullText(f), TextType.MORMON);
            t.extractNGrams();
            texts.add(t);
        }

        for(int i = 0; i < NON_MORMON_TEXTS.length; i++) {
            File f = new File("texts/non_mormon_texts/" + NON_MORMON_TEXTS[i]);
            Text t = new Text(NON_MORMON_TEXTS[i], extractFullText(f), TextType.NON_MORMON);
            t.extractNGrams();
            texts.add(t);
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
        Pattern p = Pattern.compile("(\\d+:\\d+|\")");

        try {
            Scanner s = new Scanner(f);

            while(s.hasNextLine()) {
                String line = s.nextLine();
                line = p.matcher(line).replaceAll("");
                builder.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file! " + f.toString());
        }

        return builder.toString();
    }
}
