package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

@groovy.transform.CompileStatic
public class TextLineSystem extends SortedIteratingSystem {
	
    private final ComponentMapper<RelativePositionComponent> relativePositionMapper;
    private final ComponentMapper<TextLineComponent> textLineMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<ParentComponent> parentMapper;
    
    private ImmutableArray<Entity> entities;

    private Map<Entity, Float> currentHeights;
    private Map<Entity, Integer> maxLines;
    
    public TextLineSystem() {
        super(Family.all(
                PositionComponent.class, 
                TextLineComponent.class,
                SizeComponent.class,
                RelativePositionComponent.class,
                ParentComponent.class
            ).get(), new LineComparator());
        this.relativePositionMapper = ComponentMapper.getFor(RelativePositionComponent.class);
        this.textLineMapper = ComponentMapper.getFor(TextLineComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.currentHeights = new HashMap<Entity, Float>();
        this.maxLines = new HashMap<Entity, Integer>();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.currentHeights = new HashMap<Entity, Float>();
        this.maxLines = new HashMap<Entity, Integer>();
        for(Entity entity : getEntities()) {
            ParentComponent parentComponent = parentMapper.get(entity);
            TextLineComponent line = textLineMapper.get(entity);
            
            int max = 0;
            if(maxLines.containsKey(parentComponent.parent)) {
                max = maxLines.get(parentComponent.parent);
            }
            else {
                maxLines.put(parentComponent.parent, 0);
            }
            
            if(line.lineNumber > max) {
                max = line.lineNumber;
                maxLines.put(parentComponent.parent, max);
            }
        }
        
        for(Entity entity : getEntities()) {
            RelativePositionComponent pos = relativePositionMapper.get(entity);
            TextLineComponent line = textLineMapper.get(entity);
            SizeComponent size = sizeMapper.get(entity);
            ParentComponent parentComponent = parentMapper.get(entity);
            
            int max = maxLines.get(parentComponent.parent);
            float currentHeight = 0;
            if(currentHeights.containsKey(parentComponent.parent)) {
                currentHeight = currentHeights.get(parentComponent.parent);
            }
            else {
                currentHeights.put(parentComponent.parent, 0f);
            }
            
            pos.y = -currentHeight;
            currentHeight += size.height + 4;
            currentHeights.put(parentComponent.parent, currentHeight);
        }
    }
    
    public void processEntity(Entity entity, float delta) {
        
    }
    
    private static class LineComparator implements Comparator<Entity> {
        private final ComponentMapper<TextLineComponent> lm;
		
        public LineComparator() {
            lm = ComponentMapper.getFor(TextLineComponent.class);
        }
        
        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum( - lm.get(e2).lineNumber + lm.get(e1).lineNumber);
        }
    }
}
