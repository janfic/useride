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

public class CloseOptionMenuOnMouseClickSystem extends EntitySystem {
    
    private ImmutableArray<Entity> entities, optionMenu;
   
    public CloseOptionMenuOnMouseClickSystem() {
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(CloseOptionMenuOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
        this.optionMenu = engine.getEntitiesFor(
            Family.all(OptionMenuComponent.class).get()
        );
    }
    
    public void update(float delta) {
        if(entities.size() > 0) {
            for(Entity entity : optionMenu) {
                this.engine.removeEntity(entity);
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}



