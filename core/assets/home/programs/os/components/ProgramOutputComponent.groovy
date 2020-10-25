package os.components;

import com.badlogic.ashley.core.*;

public class ProgramOutputComponent implements Component {
    public Queue<Object> output = new LinkedList<Object>();
}

