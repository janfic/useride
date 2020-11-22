package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

@groovy.transform.CompileStatic
public class NewTextLineOnKeyEnterSystem extends EntitySystem {
	
    private final ComponentMapper<KeyInputComponent> keyMapper;
    private final ComponentMapper<ParentComponent> parentMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<TextLineComponent> textLineMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<ColorComponent> colorMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;

    private ImmutableArray<Entity> entities;
    
    public NewTextLineOnKeyEnterSystem() {
        this.keyMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.textLineMapper = ComponentMapper.getFor(TextLineComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.entities = engine.getEntitiesFor(
            Family.all(
                KeyInputComponent.class, 
                TextComponent.class, 
                NewTextLineOnKeyEnterComponent.class, 
                FocusedComponent.class,
                TextLineComponent.class,
                ParentComponent.class,
                SizeComponent.class,
                ColorComponent.class
            ).get()
        );
        
    }

    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            KeyInputComponent keyInput = keyMapper.get(entity);
            ParentComponent parentComponent = parentMapper.get(entity);
            
            if(keyInput.keyTyped == 13) {
                TextComponent textComponent = textMapper.get(entity);
                ColorComponent colorComponent = colorMapper.get(entity);
                BitmapFontComponent fontComponent = fontMapper.get(entity);
                SizeComponent sizeComponent = sizeMapper.get(entity);
                TextLineComponent textLineComponent = textLineMapper.get(entity);
                
                Entity e = new Entity();
                e.add(new PositionComponent());
                e.add(new RelativePositionComponent());
                e.add(new ParentComponent(parent: parentComponent.parent))
                e.add(new BitmapFontComponent(font: fontComponent.font));
                e.add(new TextComponent(text: textComponent.text));
                e.add(new ColorComponent(color: colorComponent.color));
                e.add(new SizeComponent(width: sizeComponent.width, height: sizeComponent.height));
                e.add(new TextLineComponent(lineNumber: textLineComponent.lineNumber))
                
                this.getEngine().addEntity(e);
                
                textComponent.text = "";
                textLineComponent.lineNumber += 1;
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
    }
}

