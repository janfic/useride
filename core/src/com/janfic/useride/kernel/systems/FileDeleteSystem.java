package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;

/**
 *
 * @author janfic
 */
public class FileDeleteSystem extends EntitySystem {

    private final ComponentMapper<FileDeleteRequestComponent> deleteMapper;

    private ImmutableArray<Entity> entities;

    public FileDeleteSystem() {
        this.deleteMapper = ComponentMapper.getFor(FileDeleteRequestComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(Family.all(FileDeleteRequestComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            FileDeleteRequestComponent deleteRequest = deleteMapper.get(entity);

            FileHandle file = Gdx.files.local(deleteRequest.fileName);
            file.deleteDirectory();

            entity.remove(FileDeleteRequestComponent.class);
        }
    }
}

