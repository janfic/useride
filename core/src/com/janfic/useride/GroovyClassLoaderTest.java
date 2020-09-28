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

        GroovyCodeSource source = new GroovyCodeSource(file);

        loader.addClasspath(pack.getPath());

        Class c2 = loader.parseClass(source);
        Class c1 = loader.loadClass("os.systems.RenderSystem");
        System.out.println(c1 == c2);
        System.out.println(c1.equals(c2));
    }
}
