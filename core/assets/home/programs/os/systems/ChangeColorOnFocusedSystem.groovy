package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeColorOnFocusedSystem extends EntitySystem {
	
    private final ComponentMapper<ChangeColorOnFocusedComponent> changeMapper;
    private final ComponentMapper<ColorComponent> colorMapper;

    private ImmutableArray<Entity> focusedEntities, unfocusedEntities;

    public ChangeColorOnFocusedSystem() {
        this.changeMapper = ComponentMapper.getFor(ChangeColorOnFocusedComponent.class);
        this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.focusedEntities = engine.getEntitiesFor(
            Family.all(
                ColorComponent.class,
                ChangeColorOnFocusedComponent.class,
                FocusedComponent.class,
                FocusableComponent.class
            ).get()
        );
        this.unfocusedEntities = engine.getEntitiesFor(
            Family.all(
                ColorComponent.class,
                ChangeColorOnFocusedComponent.class,
                FocusableComponent.class
            ).exclude(
                FocusedComponent.class
            )
            .get()
        );

    }

    public void update(float delta) {
        for(Entity entity : focusedEntities) {
            ColorComponent color = colorMapper.get(entity);
            ChangeColorOnFocusedComponent change = changeMapper.get(entity);

            color.color = change.focusedColor.cpy();
        }
        
        for(Entity entity : unfocusedEntities) {
            ColorComponent color = colorMapper.get(entity);
            ChangeColorOnFocusedComponent change = changeMapper.get(entity);

            color.color = change.unfocusedColor.cpy();
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }

}
