package os.systems;

import java.util.Comparator;

import os.components.*;
import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class FocusSystem extends SortedIteratingSystem {

    private final ComponentMapper<FocusableComponent> focusableMapper;
    private final ComponentMapper<PositionComponent> positionMapper;

    private Entity focused;

    public FocusSystem() {
        super(
            Family.all(FocusableComponent.class, PositionComponent.class).get(),
            new FocusComparator()
        );
        this.focusableMapper = ComponentMapper.getFor(FocusableComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Keys.TAB) && entities.size() > 0) {
            if(focused == null) {
                focused = entities.first();
                entities.first().add(new FocusedComponent())
            }
            else {
                focused.remove(FocusedComponent.class);
                int index = entities.indexOf(focused, true);
                focused = entities.get(index + 1);
                focused.add(new FocusedComponent());
            }
        }
    }

    public void processEntity(Entity entity, float delta) {
	
    }

    public void removedFromEngine(Engine engine) {
    }

    private static class FocusComparator implements Comparator<Entity> {
        
        public int compare(Entity a, Entity b) {
            PositionComponent pa = positionMapper.get(a);
            PositionComponent pb = positionMapper.get(b);

            if (pa.y > pb.y) return 1;
            else if(pa.y < pb.y) return -1;
            else if(pa.x < pb.x) return -1;
            else if(pa.x > pb.x) return 1;
            else return 0;
        }
    }
}