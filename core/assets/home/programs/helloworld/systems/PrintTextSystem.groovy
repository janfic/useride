package helloworld.systems;

import os.components.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class PrintTextSystem extends EntitySystem {
	
    private final ComponentMapper<TextComponent> textMapper;

    private ImmutableArray<Entity> entities;

    public PrintTextSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(TextComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            TextComponent text = textMapper.get(entity);
            System.out.println(text.text);
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }

}