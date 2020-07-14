package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class RelativePositionSystem extends EntitySystem {
	
    private final ComponentMapper<PositionComponent> positionMapper;;
    private final ComponentMapper<SizeComponent> sizeMapper;;
    private final ComponentMapper<RelativePositionComponent> relativePositionMapper;;
    private final ComponentMapper<ParentComponent> parentMapper;;

    private ImmutableArray<Entity> entities;

    public RelativePositionSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.relativePositionMapper = ComponentMapper.getFor(RelativePositionComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class, RelativePositionComponent.class, ParentComponent.class).exclude(DraggingComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            PositionComponent childPosition = positionMapper.get(entity);
            RelativePositionComponent relativePosition = relativePositionMapper.get(entity);
            ParentComponent parentComponent = parentMapper.get(entity);
            
            Entity parent = parentComponent.parent;
            PositionComponent parentPosition = positionMapper.get(parent);
            SizeComponent parentSize = sizeMapper.get(parent);
            
            if(parentPosition == null) continue;
            
            if(relativePosition.unit.equals("p")) {
                childPosition.x = parentPosition.x + relativePosition.x;
                childPosition.y = parentPosition.y + relativePosition.y;
            }
            else if(relativePosition.unit.equals("%")) {
                childPosition.x = parentPosition.x + (relativePosition.x * parentSize.width * 0.01);
                childPosition.y = parentPosition.y + (relativePosition.y * parentSize.height * 0.01);
            }
            else if(relativePosition.unit.length() > 1) {
                if(relativePosition.unit.charAt(0) == 'p') {
                    childPosition.x = parentPosition.x + relativePosition.x;
                }
                else if (relativePosition.unit.charAt(0) == '%') {
                    childPosition.x = parentPosition.x + (relativePosition.x * parentSize.width * 0.01);
                }
                if(relativePosition.unit.charAt(1) == 'p') {
                    childPosition.y = parentPosition.y + relativePosition.y;
                }
                else if (relativePosition.unit.charAt(1) == '%') {
                    childPosition.y = parentPosition.y + (relativePosition.y * parentSize.height * 0.01);
                }
            }
        }
    }   
}