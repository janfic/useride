package components;

import com.badlogic.ashley.core.*;

public class TestComponent implements Component {
    public String string;
    public int integer;
    public double decimal;
    public List<String> list;
    
    public String toString() {
        return "string: " + string + "\n" +
               "integer: " + integer + "\n" + 
               "decimal: " + decimal + "\n" +
               "list: " + list;
    }
}