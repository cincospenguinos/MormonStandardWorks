package standard_works;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Main class where the magic happens.
 */
public class Main {

    private static final String[] MORMON_TEXTS = { "the_book_of_mormon", "pearl_of_great_price", "d&c" };
    private static final String[] NON_MORMON_TEXTS = { "kjv" , "the_late_war" };

    public static void main(String[] args) {
        List<Text> texts = new ArrayList();

        for (int i = 0; i < MORMON_TEXTS.length; i++) {
            File f = new File("texts/mormon_texts/" + MORMON_TEXTS[i] + ".txt");
            Text t = new Text(MORMON_TEXTS[i], extractFullText(f), TextType.MORMON);
            t.extractNGrams();
            texts.add(t);
        }

        for(int i = 0; i < NON_MORMON_TEXTS.length; i++) {
            File f = new File("texts/non_mormon_texts/" + NON_MORMON_TEXTS[i] + ".txt");
            Text t = new Text(NON_MORMON_TEXTS[i], extractFullText(f), TextType.NON_MORMON);
            t.extractNGrams();
            texts.add(t);
        }

        setupOutput();

        for (Text t : texts) {
            if (t.getType() == TextType.MORMON) {
                createFrequencyFile(t, "dataset/mormon/" + t.getTitle());
            } else {
                createFrequencyFile(t, "dataset/non_mormon/" + t.getTitle());
            }
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
                builder.append(line);
                builder.append('\n');
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file! " + f.toString());
        }

        return builder.toString();
    }

    private static void setupOutput() {
        File mormonOutput = new File("dataset/mormon");
        File nonMormonOutput = new File("dataset/non_mormon");

        if (!mormonOutput.exists())
            mormonOutput.mkdirs();

        if (!nonMormonOutput.exists())
            nonMormonOutput.mkdirs();
    }

    private static void createFrequencyFile(Text text, String filename) {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(filename + "_freq.csv"),CSVFormat.DEFAULT)) {
            printer.printRecord("string", "n", "frequency");

            NGramCollection collection = text.getNGramCollection();
            for (Map.Entry<NGram, Integer> e : collection.getFrequencies()) {
                printer.printRecord(e.getKey(), e.getKey().size(), e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
