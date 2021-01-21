package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.files.FileHandle;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import os.systems.*;
import files.components.*;

public class FileRenameSystem extends EntitySystem {
    
    private final ComponentMapper<FileComponent> fileMapper;
    private final ComponentMapper<FileRenameComponent> renameMapper;
    
    private ImmutableArray<Entity> entities;
    
    public FileRenameSystem() {
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.renameMapper = ComponentMapper.getFor(FileRenameComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileRenameComponent.class, FileComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileComponent fileComponent = fileMapper.get(entity);
            FileRenameComponent renameComponent = renameMapper.get(entity);
            FileHandle newFile = fileComponent.file.sibling(renameComponent.rename.trim());
            entity.remove(FileRenameComponent.class);
            if(renameComponent.rename.trim().equals(fileComponent.file.name())) continue;
            if(fileComponent.file.isDirectory()) {
                newFile.mkdirs();
                for(FileHandle child : fileComponent.file.list()) {
                    child.moveTo(newFile);
                }
                if(fileComponent.file.list().length == 0) {
                    fileComponent.file.deleteDirectory();
                }
            }
            else {
                fileComponent.file.moveTo(newFile);
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}