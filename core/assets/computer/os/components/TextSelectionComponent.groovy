package os.components;

import com.badlogic.ashley.core.*;

public class TextSelectionComponent implements Component {
    int textCursorIndex = -1;
    boolean insertCursor;
    int startIndex = -1, endIndex = -1;
}
