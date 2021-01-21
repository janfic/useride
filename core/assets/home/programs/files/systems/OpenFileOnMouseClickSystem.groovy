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

public class OpenFileOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<OpenFileOnMouseClickComponent> searchMapper;
    private final ComponentMapper<MouseClickEventComponent> clickMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> entities, pathEntity;
   
    public OpenFileOnMouseClickSystem() {
        this.searchMapper = ComponentMapper.getFor(OpenFileOnMouseClickComponent.class);
        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(OpenFileOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
        this.pathEntity = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class).get()
        );
    }
    
    public void update(float delta) {
        if(pathEntity.size() < 1) return;
        TextComponent text = textMapper.get(pathEntity.first());
        for(Entity entity : entities) {
            OpenFileOnMouseClickComponent openFile = searchMapper.get(entity);
            text.text = openFile.path;
            pathEntity.first().add(new FileSearchComponent());
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}


