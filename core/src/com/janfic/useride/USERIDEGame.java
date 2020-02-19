package com.janfic.useride;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.janfic.useride.kernel.systems.BootSystem;
import groovy.lang.GroovyClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class USERIDEGame extends Game {
    
    Engine engine;
    
    @Override
    public void create() {
        engine = new Engine();
        engine.addSystem(new BootSystem());
        
        GroovyClassLoader cl = new GroovyClassLoader();
        FileHandle root = Gdx.files.local("test");
        cl.addClasspath(root.path());
        try {
            cl.loadClass("Test");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(USERIDEGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
    }
    
    @Override
    public void dispose() {
        engine.removeAllEntities();
    }
}
