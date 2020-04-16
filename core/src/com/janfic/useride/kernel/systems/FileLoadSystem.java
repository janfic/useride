package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.FileLoadRequestComponent;

/**
 *
 * @author janfic
 */
public class FileLoadSystem extends EntitySystem {

    /**
     * A Entity to Component Mapper that allows quick retrieval of
     * FileSaveRequestComponent from entities.
     */
    private final ComponentMapper<FileLoadRequestComponent> fileLoadRequestMapper;

    /**
     * Entities that have the desired components, given by the engine.
     */
    private ImmutableArray<Entity> entities;

    public FileLoadSystem() {
        this.fileLoadRequestMapper = ComponentMapper.getFor(FileLoadRequestComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(Family.all(FileLoadRequestComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            FileLoadRequestComponent loadRequest = fileLoadRequestMapper.get(entity);

            FileComponent fileComponent = new FileComponent();
            fileComponent.file = Gdx.files.local(loadRequest.fileName);

            entity.remove(FileLoadRequestComponent.class);
            entity.add(fileComponent);
        }
    }

}
