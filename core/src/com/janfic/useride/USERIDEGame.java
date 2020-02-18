package com.janfic.useride;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.janfic.useride.kernel.systems.BootSystem;

public class USERIDEGame extends ApplicationAdapter {
    
    Engine engine;
    
    @Override
    public void create() {
        engine = new Engine();
        engine.addSystem(new BootSystem());
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
    }
    
    @Override
    public void dispose() {
        
    }
}
