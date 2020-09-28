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

public class OpenFileOnMouseDoubleClickSystem extends EntitySystem {
        
    private final ComponentMapper<OpenFileOnMouseDoubleClickComponent> searchMapper;
    private final ComponentMapper<MouseClickEventComponent> clickMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> entities, pathEntity;
   
    public OpenFileOnMouseDoubleClickSystem() {
        this.searchMapper = ComponentMapper.getFor(OpenFileOnMouseDoubleClickComponent.class);
        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(OpenFileOnMouseDoubleClickComponent.class, MouseClickEventComponent.class).get()
        );
        this.pathEntity = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class).get()
        );
    }
    
    public void update(float delta) {
        if(pathEntity.size() < 1) return;
        TextComponent text = textMapper.get(pathEntity.first());
        for(Entity entity : entities) {
            OpenFileOnMouseDoubleClickComponent openFile = searchMapper.get(entity);
            MouseClickEventComponent clickEvent = clickMapper.get(entity);
            if(clickEvent.count == 2 ) {
                String path = openFile.path;
                text.text = path;
                pathEntity.first().add(new FileSearchComponent());
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}

