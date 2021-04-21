package ui.systems;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeBackgroundColorOnFocusedSystem extends EntitySystem {
	
    private final ComponentMapper<ChangeBackgroundColorOnFocusedComponent> changeMapper;
    private final ComponentMapper<BackgroundColorComponent> colorMapper;

    private ImmutableArray<Entity> focusedEntities, unfocusedEntities;

    public ChangeBackgroundColorOnFocusedSystem() {
        this.changeMapper = ComponentMapper.getFor(ChangeBackgroundColorOnFocusedComponent.class);
        this.colorMapper = ComponentMapper.getFor(BackgroundColorComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.focusedEntities = engine.getEntitiesFor(
            Family.all(
                BackgroundColorComponent.class,
                ChangeBackgroundColorOnFocusedComponent.class,
                FocusedComponent.class,
                FocusableComponent.class
            ).get()
        );
        this.unfocusedEntities = engine.getEntitiesFor(
            Family.all(
                BackgroundColorComponent.class,
                ChangeBackgroundColorOnFocusedComponent.class,
                FocusableComponent.class
            ).exclude(
                FocusedComponent.class
            )
            .get()
        );

    }

    public void update(float delta) {
        for(Entity entity : focusedEntities) {
            BackgroundColorComponent color = colorMapper.get(entity);
            ChangeBackgroundColorOnFocusedComponent change = changeMapper.get(entity);

            color.color = change.focused.cpy();
        }
        
        for(Entity entity : unfocusedEntities) {
            BackgroundColorComponent color = colorMapper.get(entity);
            ChangeBackgroundColorOnFocusedComponent change = changeMapper.get(entity);

            color.color = change.unfocused.cpy();
        }
    }
}
