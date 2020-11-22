package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

@groovy.transform.CompileStatic
public class NewTextLineSystem extends EntitySystem {
	
    private final ComponentMapper<TextLineComponent> textLineMapper;
    private final ComponentMapper<NewTextLineComponent> newLineMapper;
    private final ComponentMapper<ParentComponent> parentMapper;

    private Map<Entity, Integer> maxLines;
    private ImmutableArray<Entity> entities, lines;
    
    public NewTextLineSystem() {
        this.newLineMapper = ComponentMapper.getFor(NewTextLineComponent.class);
        this.textLineMapper = ComponentMapper.getFor(TextLineComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.lines = engine.getEntitiesFor(
            Family.all(
                ParentComponent.class,
                TextLineComponent.class
            ).get()
        );
        this.entities = engine.getEntitiesFor(
            Family.all(
                NewTextLineComponent.class,
                ParentComponent.class
            ).get()
        );
        this.maxLines = new HashMap<Entity, Integer>();
    }

    @Override
    public void update(float delta) {
        this.maxLines = new HashMap<Entity, Integer>();
        for(Entity entity : this.lines) {
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
        
        for(Entity entity : entities) {
            ParentComponent parentComponent = parentMapper.get(entity);
            TextLineComponent textLine = new TextLineComponent();
            int max = 0;
            if(maxLines.get(parentComponent.parent) != null ) {
                max = maxLines.get(parentComponent.parent) + 1;
            }
            textLine.lineNumber = max;
            maxLines.put(parentComponent.parent, max);
            entity.add(textLine);
            entity.remove(NewTextLineComponent.class);
        }
    }
    
    public void removedFromEngine(Engine engine) {
        this.maxLines = null;
    }
}

