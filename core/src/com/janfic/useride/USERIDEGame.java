package com.janfic.useride;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.janfic.useride.kernel.systems.BootSystem;

public class USERIDEGame extends Game {

    Engine engine;
    Texture splashTexture;
    SpriteBatch batch;
    float splashScreenWait = 3;

    @Override
    public void create() {
        engine = new Engine();
        Gdx.input.setInputProcessor(new InputMultiplexer());
        splashTexture = new Texture("splash.png");
        batch = new SpriteBatch();
        engine.addSystem(new BootSystem());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (splashScreenWait > 0) {
            batch.begin();
            batch.draw(splashTexture, 1980 / 2 - splashTexture.getWidth(), 1080 / 2 - splashTexture.getHeight(), splashTexture.getWidth() * 2, splashTexture.getHeight() * 2);
            batch.end();
            splashScreenWait -= Gdx.graphics.getDeltaTime();
        } else {
            engine.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
    }
}
