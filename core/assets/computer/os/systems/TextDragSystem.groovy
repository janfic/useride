package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

@CompileStatic
public class TextDragSystem extends EntitySystem {
	
    private final ComponentMapper<DraggingComponent> draggingMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<ViewportComponent> viewportMapper;
    private final ComponentMapper<DragableComponent> dragableMapper;
    private final ComponentMapper<AlignmentComponent> alignMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<HitBoxComponent> hitboxMapper;
    private final ComponentMapper<TextSelectionComponent> selectMapper;

    private ImmutableArray<Entity> entities;

    private GlyphLayout layout;
    
    public TextDragSystem() {
        this.draggingMapper = ComponentMapper.getFor(DraggingComponent.class);
        this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
        this.dragableMapper = ComponentMapper.getFor(DragableComponent.class);
        this.hitboxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.alignMapper = ComponentMapper.getFor(AlignmentComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.selectMapper = ComponentMapper.getFor(TextSelectionComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                PositionComponent.class,
                DragableComponent.class,
                DraggingComponent.class,
                TextDragComponent.class,
                TextSelectionComponent.class,
                TextComponent.class,
                HitBoxComponent.class,
                AlignmentComponent.class,
                SizeComponent.class,
                BitmapFontComponent.class
            ).get()
        );
        this.layout = new GlyphLayout();
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            
            DraggingComponent draggingComponent = draggingMapper.get(entity);
            PositionComponent positionComponent = positionMapper.get(entity);
            AlignmentComponent alignmentComponent = alignMapper.get(entity);
            BitmapFontComponent fontComponent = fontMapper.get(entity);
            TextComponent textComponent = textMapper.get(entity);
            SizeComponent sizeComponent = sizeMapper.get(entity);
            TextSelectionComponent selectComponent = selectMapper.get(entity);
            
            layout.setText(fontComponent.font, textComponent.text, Color.BLACK, sizeComponent.width, alignmentComponent.alignment, true);
            
            String[] sections = buildSections(textComponent.text, layout);
            
            
            int index = 0;
            int run = 0;
            for(int i = 0; i < sections.length; i++) {
                float xStart = 0;
                if(sections[i].trim().equals("")) {
                    if(
                        draggingComponent.startX > positionComponent.x + xStart 
                        && draggingComponent.startX < positionComponent.x + xStart + fontComponent.font.getData().spaceXadvance
                        && draggingComponent.startY > (sizeComponent.height + positionComponent.y) - (i + 1) * fontComponent.font.getLineHeight()
                        && draggingComponent.startY < (sizeComponent.height + positionComponent.y) - (i) * fontComponent.font.getLineHeight()) {
                        selectComponent.textCursorIndex = index;
                    }
                    index += sections[i].length();
                    run -= 1;
                }
                else {
                    for(int j = 0; j < sections[i].length(); j++) {
                        if(
                            draggingComponent.startX > positionComponent.x + xStart 
                            && draggingComponent.startX < positionComponent.x + xStart + layout.runs.get(run).xAdvances.get(j + 1)
                            && draggingComponent.startY > (sizeComponent.height + positionComponent.y) - (i + 1) * fontComponent.font.getLineHeight()
                            && draggingComponent.startY < (sizeComponent.height + positionComponent.y) - (i) * fontComponent.font.getLineHeight()) {
                            selectComponent.textCursorIndex = index;
                        }
                        index += 1;
                        xStart += layout.runs.get(run).xAdvances.get(j + 1);
                    }
                    index += 1;
                }
                run += 1;
            }
        }
    }
    
    private String[] buildSections(String text, GlyphLayout layout) {
        ArrayList<String> r = new ArrayList<String>();
        int runIndex = 0;
        int i = 0;
        String currentString = ""
        while(i < text.length() && runIndex < layout.runs.size) {
            int indexOfGlyphs = text.indexOf(glyphsToString(layout.runs.get(runIndex).glyphs), i);
            
            if(indexOfGlyphs > i) {
                int newLines = 0;
                for(int j = i; j < indexOfGlyphs; j++) {
                    if(text.charAt(j) == '\n') {
                        newLines++;
                        if(newLines > 0 && j != i && j != indexOfGlyphs) {
                            //System.out.println("[" + "\\n" + "]");
                            r.add("\n");
                        }
                    }
                }
            }
            
            currentString = text.substring(indexOfGlyphs, indexOfGlyphs + layout.runs.get(runIndex).glyphs.size)
            i = indexOfGlyphs + layout.runs.get(runIndex).glyphs.size;
            runIndex++;
            //System.out.println("[" + currentString + "]");
            r.add(currentString);
        }
        while(i < text.length()) { 
            if(text.charAt(i) == '\n') {
                //System.out.println("[" + "\\n" + "]");
                r.add("\n");
            }
            i++;
        }
        return r.toArray();
    }
    
    private String glyphsToString(Array<BitmapFont.Glyph> glyphs) {
        String s = "";
        for( int i = 0; i < glyphs.size; i++) {
            s += (char) glyphs.get(i).id;
        }
        return s;
    }
}

