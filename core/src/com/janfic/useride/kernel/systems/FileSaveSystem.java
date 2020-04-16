package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.FileSaveRequestComponent;

/**
 * FileSaveSystem: An Entity System that handles file save requests.
 *
 * An entity must contain a FileSaveRequestComponent and a FileComponent in
 * order to save a file to the players os. This system handles entities that
 * have such components and saves the file to the system
 *
 * @author janfic
 */
public class FileSaveSystem extends EntitySystem {

    /**
     * A Entity to Component Mapper that allows quick retrieval of
     * FileSaveRequestComponent from entities.
     */
    private final ComponentMapper<FileSaveRequestComponent> fileSaveRequestMapper;

    /**
     * A Entity to Component Mapper that allows quick retrieval of FileComponent
     * from entities.
     */
    private final ComponentMapper<FileComponent> fileMapper;

    /**
     * Entities that have the desired components, given by the engine.
     */
    private ImmutableArray<Entity> entities;

    public FileSaveSystem() {
        this.fileSaveRequestMapper = ComponentMapper.getFor(FileSaveRequestComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
                Family.all(
                        FileSaveRequestComponent.class,
                        FileComponent.class
                ).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            FileSaveRequestComponent saveRequest = fileSaveRequestMapper.get(entity);
            FileComponent fileComponent = fileMapper.get(entity);

            fileComponent.file.writeBytes(saveRequest.data, false);

            entity.remove(FileSaveRequestComponent.class);
        }
    }
}
