package com.janfic.useride.kernel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.*;

public class ProgramEntityInjectionComponent implements Component {
    public List<Entity> entities = new ArrayList<Entity>();
}
