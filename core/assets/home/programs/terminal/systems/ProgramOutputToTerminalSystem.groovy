package terminal.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.*;
import terminal.components.*;
import os.components.*;
import os.systems.*;

public class ProgramOutputToTerminalSystem extends EntitySystem {	

    private final ComponentMapper<ProgramOutputComponent> outputMapper;
    private final ComponentMapper<TextLineComponent> textLineMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<ParentComponent> parentMapper;
    private final ComponentMapper<ColorComponent> colorMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    
    private ImmutableArray<Entity> entities, textInput;
    
    public ProgramOutputToTerminalSystem() {
        this.outputMapper = ComponentMapper.getFor(ProgramOutputComponent.class);
        this.textLineMapper = ComponentMapper.getFor(TextLineComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
        this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
    }    
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ProgramOutputComponent.class
            ).get()
        );
        this.textInput = engine.getEntitiesFor(
            Family.all(
                KeyInputComponent.class,
                TextComponent.class,
                TextLineComponent.class,
                BitmapFontComponent.class,
                SizeComponent.class,
                ColorComponent.class, 
                ParentComponent.class,
            ).get()
        );
    }
    
    public void update(float delta) {
        if(textInput.size() < 1) return;
        Entity commandIn = textInput.first();
        
        ParentComponent parentComponent = parentMapper.get(commandIn);
        TextComponent textComponent = textMapper.get(commandIn);
        ColorComponent colorComponent = colorMapper.get(commandIn);
        BitmapFontComponent fontComponent = fontMapper.get(commandIn);
        SizeComponent sizeComponent = sizeMapper.get(commandIn);
        TextLineComponent textLineComponent = textLineMapper.get(commandIn);
        
        
        for(Entity entity : entities) {
            ProgramOutputComponent output = outputMapper.get(entity);
            
            if(output.output.size() > 0) {
                
                Entity e = new Entity();
                e.add(new PositionComponent());
                e.add(new RelativePositionComponent());
                e.add(new ParentComponent(parent: parentComponent.parent))
                e.add(new BitmapFontComponent(font: fontComponent.font));
                Object t = output.output.poll();
                if(t == null || t.toString().length() == 0) t = " ";
                e.add(new TextComponent(text: t));
                e.add(new ColorComponent(color: colorComponent.color));
                e.add(new SizeComponent(width: sizeComponent.width, height: sizeComponent.height));
                e.add(new TextLineComponent(lineNumber: textLineComponent.lineNumber))
                
                this.getEngine().addEntity(e);
                
                textLineComponent.lineNumber += 1
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}