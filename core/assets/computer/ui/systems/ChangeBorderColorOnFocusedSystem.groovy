package ui.systems;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeBorderColorOnFocusedSystem extends EntitySystem {
	
    private final ComponentMapper<ChangeBorderColorOnFocusedComponent> changeMapper;
    private final ComponentMapper<BorderComponent> colorMapper;

    private ImmutableArray<Entity> focusedEntities, unfocusedEntities;

    public ChangeBorderColorOnFocusedSystem() {
        this.changeMapper = ComponentMapper.getFor(ChangeBorderColorOnFocusedComponent.class);
        this.colorMapper = ComponentMapper.getFor(BorderComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.focusedEntities = engine.getEntitiesFor(
            Family.all(
                BorderComponent.class,
                ChangeBorderColorOnFocusedComponent.class,
                FocusedComponent.class,
                FocusableComponent.class
            ).get()
        );
        this.unfocusedEntities = engine.getEntitiesFor(
            Family.all(
                BorderComponent.class,
                ChangeBorderColorOnFocusedComponent.class,
                FocusableComponent.class
            ).exclude(
                FocusedComponent.class
            )
            .get()
        );

    }

    public void update(float delta) {
        for(Entity entity : focusedEntities) {
            BorderComponent color = colorMapper.get(entity);
            ChangeBorderColorOnFocusedComponent change = changeMapper.get(entity);
            color.color = change.focused.cpy();
        }
        
        for(Entity entity : unfocusedEntities) {
            BorderComponent color = colorMapper.get(entity);
            ChangeBorderColorOnFocusedComponent change = changeMapper.get(entity);

            color.color = change.unfocused.cpy();
        }
    }
}
