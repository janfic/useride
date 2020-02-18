/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Clipboard;
import com.janfic.useride.kernel.components.FileLoadRequestComponent;
import com.janfic.useride.kernel.systems.FileLoadSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janfc
 */
public class FileLoadSystemTest {

    static Engine engine;
    static Entity entity;

    @BeforeClass
    public static void setUpClass() {

        engine = new Engine();
        entity = new Entity();

        FileLoadRequestComponent loadRequest = new FileLoadRequestComponent();
        loadRequest.fileName = "tests/FileLoadSystemTest.txt";

        entity.add(loadRequest);
        engine.addEntity(entity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addedToEngine method, of class FileLoadSystem.
     */
    @Test
    public void test() {
        //FileLoadSystem instance = new FileLoadSystem();
        //engine.addSystem(instance);
        //engine.update(0);
    }

}
