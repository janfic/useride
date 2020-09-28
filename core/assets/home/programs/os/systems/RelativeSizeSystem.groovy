package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class RelativeSizeSystem extends EntitySystem {
	
    private final ComponentMapper<SizeComponent> sizeMapper;;
    private final ComponentMapper<RelativeSizeComponent> relativeSizeMapper;;
    private final ComponentMapper<ParentComponent> parentMapper;;

    private ImmutableArray<Entity> entities;

    public RelativeSizeSystem() {
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.relativeSizeMapper = ComponentMapper.getFor(RelativeSizeComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(SizeComponent.class, RelativeSizeComponent.class, ParentComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            SizeComponent childSize = sizeMapper.get(entity);
            RelativeSizeComponent relativeSize = relativeSizeMapper.get(entity);
            ParentComponent parentComponent = parentMapper.get(entity);
            
            Entity parent = parentComponent.parent;
            SizeComponent parentSize = sizeMapper.get(parent);
            
            if(parentSize == null) continue;
            
            if(relativeSize.unit.equals("p")) {
                childSize.width = parentSize.width + relativeSize.width;
                childSize.height = parentSize.height + relativeSize.height;
            }
            else if(relativeSize.unit.equals("%")) {
                childSize.width = (relativeSize.width * parentSize.width * 0.01);
                childSize.height = (relativeSize.height * parentSize.height * 0.01);
            }
            else if(relativeSize.unit.length() > 1) {
                if(relativeSize.unit.charAt(0) == 'p') {
                    childSize.width = parentSize.width + relativeSize.width;
                }
                else if (relativeSize.unit.charAt(0) == '%') {
                    childSize.width = (relativeSize.width * parentSize.width * 0.01);
                }
                if(relativeSize.unit.charAt(1) == 'p') {
                    childSize.height = parentSize.height + relativeSize.height;
                }
                else if (relativeSize.unit.charAt(1) == '%') {
                    childSize.height = (relativeSize.height * parentSize.height * 0.01);
                }
            }
        }
    }
}