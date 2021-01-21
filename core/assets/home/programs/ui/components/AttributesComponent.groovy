package ui.components;

import com.badlogic.ashley.core.*;

import org.jsoup.nodes.Attributes;

public class AttributesComponent implements Component {
    Attributes attributes;
    public AttributesComponent() {
        this.attributes = new Attributes();
    }
}