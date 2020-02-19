package com.janfic.useride.kernel.components;

import com.badlogic.ashley.core.Component;
import groovy.lang.GroovyClassLoader;

public class ClassLoaderComponent implements Component {
    public GroovyClassLoader classLoader;
}
