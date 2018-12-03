package standard_works;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class where the magic happens.
 */
public class Main {

    private static final String[] MORMON_TEXTS = {};
    private static final String[] NON_MORMON_TEXTS = { "kjv.txt" };

    public static void main(String[] args) {
        List<Text> texts = new ArrayList();

        for(int i = 0; i < NON_MORMON_TEXTS.length; i++) {
            File f = new File("texts/non_mormon_texts/" + NON_MORMON_TEXTS[i]);
            texts.add(new Text(NON_MORMON_TEXTS[i], f, TextType.NON_MORMON));
        }
    }
}
