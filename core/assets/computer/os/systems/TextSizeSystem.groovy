package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

@groovy.transform.CompileStatic
public class TextSizeSystem extends EntitySystem {
	
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<AlignmentComponent> alignMapper;
    
    private ImmutableArray<Entity> entities;
    
    private GlyphLayout layout;
    
    public TextSizeSystem() {
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.alignMapper = ComponentMapper.getFor(AlignmentComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                SizeComponent.class,
                BitmapFontComponent.class,
                TextComponent.class
            ).get()
        );
        this.layout = new GlyphLayout();
    }

    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            SizeComponent size = sizeMapper.get(entity);
            TextComponent text = textMapper.get(entity);
            BitmapFontComponent fontComponent = fontMapper.get(entity);
            AlignmentComponent alignmentComponent = alignMapper.get(entity);
            
            BitmapFont font = fontComponent.font;
            layout.setText(font, text.text, Color.BLACK, size.width, (int) (alignmentComponent == null ? Align.left : alignmentComponent.alignment), true);
            
            size.height = layout.height;
            
            layout.reset();
        }
    }
}