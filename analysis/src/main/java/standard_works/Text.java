package standard_works;

import java.io.File;

/**
 * Text that we are performing analysis on.
 */
public class Text {

    private String title;
    private TextType type;

    public Text(String _title, File _file, TextType _type) {
        title = _title;
        type = _type;
    }
}
