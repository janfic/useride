package terminal.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.math.*;
import terminal.components.*;
import os.components.*;
import os.systems.*;

public class CommandPrefixSystem extends EntitySystem {	

    private final ComponentMapper<CommandComponent> commandMapper;
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> entities;
    
    public CommandPrefixSystem() {
        this.commandMapper = ComponentMapper.getFor(CommandComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }    
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                CommandComponent.class,
                TextComponent.class
            ).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            TextComponent textComponent = textMapper.get(entity);
            CommandComponent command = commandMapper.get(entity);
            
            if(textComponent.text != null && textComponent.text.contains(command.location)) {
                command.text = textComponent.text.substring(textComponent.text.indexOf(command.location) + command.location.length());
            }
            else {
                textComponent.text = command.location;
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}
