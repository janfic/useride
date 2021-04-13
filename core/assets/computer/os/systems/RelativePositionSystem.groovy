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
    
    private Set<Entity> marked;
    
    public RelativePositionSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.relativePositionMapper = ComponentMapper.getFor(RelativePositionComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
        this.marked = new HashSet<Entity>();
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class, RelativePositionComponent.class, ParentComponent.class).exclude(DraggingComponent.class).get()
        );
    }

    public void update(float delta) {
        this.marked.clear();
        for(Entity entity : entities) {
            processEntity(entity);
        } 
    }   
    
    public void processEntity(Entity entity) {
        if(this.marked.contains(entity)) return;
        this.marked.add(entity);
        
        PositionComponent childPosition = positionMapper.get(entity);
        SizeComponent childSize = sizeMapper.get(entity);
        RelativePositionComponent relativePosition = relativePositionMapper.get(entity);
        ParentComponent parentComponent = parentMapper.get(entity);
            
        Entity parent = relativePosition.parent == null ? parentComponent.parent : relativePosition.parent;
        PositionComponent parentPosition = positionMapper.get(parent);
        SizeComponent parentSize = sizeMapper.get(parent);
            
        if(Family.all(PositionComponent.class, RelativePositionComponent.class, ParentComponent.class).exclude(DraggingComponent.class).get().matches(parent)) processEntity(parent);
        if(parentPosition == null) return;    
        
        if(relativePosition.unit.equals("p")) {
            childPosition.x = parentPosition.x * relativePosition.parentMultiplier + relativePosition.x;
            childPosition.y = parentPosition.y * relativePosition.parentMultiplier + relativePosition.y;
        }
        else if(relativePosition.unit.equals("%")) {
            childPosition.x = parentPosition.x * relativePosition.parentMultiplier + (relativePosition.x * parentSize.width * 0.01);
            childPosition.y = parentPosition.y * relativePosition.parentMultiplier + (relativePosition.y * parentSize.height * 0.01);
        }
        else if(relativePosition.unit.equals("s") && childSize != null) {
            childPosition.x = parentPosition.x * relativePosition.parentMultiplier + (relativePosition.x * childSize.width * 0.01);
            childPosition.y = parentPosition.y * relativePosition.parentMultiplier + (relativePosition.y * childSize.height * 0.01);
        }
        else if(relativePosition.unit.length() > 1) {
            if(relativePosition.unit.charAt(0) == 'p') {
                childPosition.x = parentPosition.x * relativePosition.parentMultiplier + relativePosition.x;
            }
            else if (relativePosition.unit.charAt(0) == '%') {
                childPosition.x = parentPosition.x * relativePosition.parentMultiplier + (relativePosition.x * parentSize.width * 0.01);
            }
            else if (relativePosition.unit.charAt(0) == 's') {
                childPosition.x = parentPosition.x * relativePosition.parentMultiplier + (relativePosition.x * childSize.width * 0.01);
            }
            if(relativePosition.unit.charAt(1) == 'p') {
                childPosition.y = parentPosition.y * relativePosition.parentMultiplier + relativePosition.y;
            }
            else if (relativePosition.unit.charAt(1) == '%') {
                childPosition.y = parentPosition.y * relativePosition.parentMultiplier + (relativePosition.y * parentSize.height * 0.01);
            }
            else if (relativePosition.unit.charAt(1) == 's') {
                childPosition.y = parentPosition.y * relativePosition.parentMultiplier + (relativePosition.y * childSize.height * 0.01);
            }
            if(relativePosition.unit.length() > 2) {
                if(relativePosition.unit.charAt(2) == 'p') {
                    childPosition.z = parentPosition.z * relativePosition.parentMultiplier + relativePosition.z;
                }
                else if (relativePosition.unit.charAt(2) == '%') {
                    childPosition.z = parentPosition.z * relativePosition.parentMultiplier + (relativePosition.z * parentSize.height * 0.01);
                } 
            }
        }
    } 
}