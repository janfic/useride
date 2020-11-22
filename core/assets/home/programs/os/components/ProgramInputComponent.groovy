package os.components;

import com.badlogic.ashley.core.*;

public class ProgramInputComponent implements Component {
    public Queue<Object> input = new LinkedList<Object>();
}

