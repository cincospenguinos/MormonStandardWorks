package standard_works;

import java.io.File;

/**
 * Text that we are performing analysis on.
 */
public class Text {

    private String title;
    private String fullText;
    private TextType type;

    public Text(String _title, String _fullText, TextType _type) {
        title = _title;
        type = _type;
        fullText = _fullText;
    }

    public void extractNGrams() {

    }
}