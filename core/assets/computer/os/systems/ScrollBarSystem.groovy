package os.systems;

import java.util.Comparator;

import os.components.*;
import com.badlogic.gdx.Gdx ;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class ScrollBarSystem extends EntitySystem {

    private final ComponentMapper<ScrollBarComponent> barMapper;
    private final ComponentMapper<ScrollBarRootComponent> rootMapper;
    private final ComponentMapper<ScrollContentsComponent> contentsMapper;
    private final ComponentMapper<RelativePositionComponent> relativePositionMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<HitBoxComponent> boxMapper;

    private ImmutableArray<Entity> scrollBars;
    
    public ScrollBarSystem() {
        this.barMapper = ComponentMapper.getFor(ScrollBarComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.boxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.relativePositionMapper = ComponentMapper.getFor(RelativePositionComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.scrollBars = engine.getEntitiesFor(
            Family.all(ScrollBarComponent.class, SizeComponent.class, HitBoxComponent.class).get()
        );
    }

    public void update(float delta) {
        for( Entity entity : scrollBars) {
            ScrollBarComponent bar = barMapper.get(entity);
            SizeComponent barSize = sizeMapper.get(entity);
            HitBoxComponent box = boxMapper.get(entity);
            
            Entity root = bar.scrollRoot;
            Entity contents = bar.scrollContents;
            
            if (root == null || contents == null) continue;
             
            SizeComponent rootSize = sizeMapper.get(root);
            SizeComponent contentsSize = sizeMapper.get(contents);
            RelativePositionComponent rp = relativePositionMapper.get(contents);
            
            if(rootSize == null || contentsSize == null) continue;
            
            barSize.height = rootSize.height * (rootSize.height / contentsSize.height);
            if(barSize.height > rootSize.height) barSize.height = rootSize.height;
            box.rectangle.setHeight(barSize.height);
            rp.parentMultiplier =  - contentsSize.height / rootSize.height;
            rp.y = (contentsSize.height > rootSize.height ? contentsSize.height : rootSize.height);
        }
    }


    public void removedFromEngine(Engine engine) {
    }
}