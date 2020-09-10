/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.janfic.useride;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.transform.CompileStatic;
import java.io.File;
import java.util.Scanner;

@CompileStatic

/**
 *
 * @author Jan Fic
 */
public class GroovyClassLoaderTest {

    public static void main(String[] args) throws Exception {
        GroovyClassLoader loader = new GroovyClassLoader();
        File file = new File("test/packtwo/TestClassTwo.groovy");
        File file2 = new File("test/packone/TestClass.groovy");
        File pack = new File("test");

        GroovyCodeSource source1 = new GroovyCodeSource(file);
        GroovyCodeSource source2 = new GroovyCodeSource(file2);

        loader.addClasspath(pack.getPath());
        Class f2 = loader.parseClass(source2);
        System.out.println(loader.parseClass(source1));
        Scanner scan = new Scanner(System.in);
        System.out.println(loader.parseClass(source2).getConstructor().newInstance().getClass().equals(f2));
    }
}
