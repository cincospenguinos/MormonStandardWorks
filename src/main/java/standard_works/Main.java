package standard_works;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Main class where the magic happens.
 *
 */
public class Main {

    private static final String TEXTS_DIRECTORY = "texts/";
    private static final String OUTPUT_DIRECTORY = "dataset/";

    public static void main(String[] args) {
        setupOutput();

        List<Text> mormonTexts = new ArrayList<>();
        List<Text> nonMormonTexts = new ArrayList<>();
        List<Text> allTexts = new ArrayList<>();

        for (String filename : Objects.requireNonNull(new File(TEXTS_DIRECTORY).list())) {
            Text t = extractFullText(filename);

            if (t.getType().equals(TextType.MORMON)) {
                mormonTexts.add(t);
            } else {
                nonMormonTexts.add(t);
            }

            allTexts.add(t);
        }

        // Start processing nGrams
        ExecutorService pool = Executors.newFixedThreadPool(7);

        for (Text t : allTexts) {
            pool.submit(t::extractNGrams);
        }

        // While that's going, let's create an overview file
        System.out.println("Setup the books overview file...");
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(OUTPUT_DIRECTORY + "books_overview.csv"), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("textName", "filename", "isMormon");

            for (Text t : allTexts) {
                csvPrinter.printRecord(t.getTitle(), t.getFileName(), t.getType() == TextType.MORMON);
            }

            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // We'll wait at most one minute until everything is processed
        try {
            System.out.println("Waiting on nGram processing...");
            pool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Processing intersection files...");
        for (Text mormonText : mormonTexts) {
            for (Text nonMormonText : nonMormonTexts) {
                pool.submit(() -> {
                    String newFileName = mormonText.getFileName() + "_" + nonMormonText.getFileName() + ".csv";

                    try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(OUTPUT_DIRECTORY + newFileName), CSVFormat.DEFAULT)) {
                        csvPrinter.printRecord("nGram", "size", "mormonFreq", "nonMormonFreq");

                        Map<NGram, Integer[]> intersection = NGramCollection.frequencyIntersection(mormonText.getNGramCollection(), nonMormonText.getNGramCollection());

                        for(Map.Entry<NGram, Integer[]> e : intersection.entrySet()) {
                            csvPrinter.printRecord(e.getKey(), e.getKey().size(), e.getValue()[0], e.getValue()[1]);
                        }

                        csvPrinter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                });
            }
        }

        try {
            pool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        pool.shutdown();
    }

    /**
     * Helper method. Returns the full text of the file provided.
     *
     * @param filename - name of file to extract from
     * @return String array of the text's name and the full text
     */
    private static Text extractFullText(String filename) {
        File f = new File(TEXTS_DIRECTORY + filename);

        String textName = null;
        TextType textType = null;
        StringBuilder builder = new StringBuilder();

        Pattern p = Pattern.compile("(\\d+:\\d+|\")");

        try {
            Scanner s = new Scanner(f);

            while(s.hasNextLine()) {
                String line = s.nextLine();

                if (textName == null) {
                    String[] firstLine = line.trim().split(":");
                    textName = firstLine[0].trim();
                    textType = TextType.valueOf(firstLine[1]);
                } else {
                    line = p.matcher(line).replaceAll("");
                    builder.append(line);
                    builder.append('\n');
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find file! " + f.toString());
        }

        return new Text(textName, builder.toString(), textType);
    }

    private static void setupOutput() {
        File outputDir = new File(OUTPUT_DIRECTORY);

        if (!outputDir.exists())
            outputDir.mkdir();
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
