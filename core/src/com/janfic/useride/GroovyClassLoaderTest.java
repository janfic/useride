package com.janfic.useride;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import java.io.File;

/**
 *
 * @author Jan Fic
 */
public class GroovyClassLoaderTest {

    public static void main(String[] args) throws Exception {
        GroovyClassLoader loader = new GroovyClassLoader();
        File file = new File("assets/home/programs/os/systems/RenderSystem.groovy");
        File pack = new File("assets/home/programs");

        GroovyCodeSource source1 = new GroovyCodeSource(file);

        loader.addClasspath(pack.getPath());

        System.out.println(loader.loadClass("os.systems.RenderSystem") == loader.parseClass(source1));
        System.out.println();

    }
}
