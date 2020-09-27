package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.files.*;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import files.components.*;

public class FilePasteSystem extends EntitySystem {
        
    private final ComponentMapper<FileComponent> fileMapper;
    
    private ImmutableArray<Entity> pasteEntities, copyEntities;
   
    private ArrayList<FileHandle> copied;
    
    public FilePasteSystem() {
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.pasteEntities = engine.getEntitiesFor(
            Family.all(FilePasteComponent.class, FileComponent.class).get()
        );
        this.copyEntities = engine.getEntitiesFor(
            Family.all(FileCopyComponent.class, FileComponent.class).get()
        );
        this.copied = new ArrayList<FileHandle>();
    }
    
    public void update(float delta) {
        //if(copyEntities.size() < 1 || pasteEntities.size() < 1) return;
        for(Entity copy : copyEntities) {
            FileComponent copyFile = fileMapper.get(copy);
            this.copied.add(copyFile.file);
            copy.remove(FileCopyComponent.class);
            System.out.println(copied);
        }
        
        for(Entity paste : pasteEntities) {
            FileComponent pasteFile = fileMapper.get(paste);
            for(FileHandle copyFile : copied) {
                copyFile.copyTo(pasteFile.file);
            }
            copied.clear();
            paste.remove(FilePasteComponent.class);
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}