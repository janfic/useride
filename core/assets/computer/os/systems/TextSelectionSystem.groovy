package os.systems;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;

public class TextSelectionSystem extends EntitySystem {
    private final ComponentMapper<TextSelectionComponent> selectMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<AlignmentComponent> alignMapper;
    private final ComponentMapper<ParentComponent> parentMapper;
    private final ComponentMapper<ChildrenComponent> childrenMapper;
    private final ComponentMapper<HitBoxComponent> hitBoxMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<RelativePositionComponent> relativePositionMapper;
    
    private ImmutableArray<Entity> entities, highLightEntities;
    private Family textEntitiesFamily;
    private Family selectBoxFamily;
    
    private GlyphLayout layout;
    private float blinker;
    
    public TextSelectionSystem() {
        this.selectMapper = ComponentMapper.getFor(TextSelectionComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.alignMapper = ComponentMapper.getFor(AlignmentComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
        this.childrenMapper = ComponentMapper.getFor(ChildrenComponent.class);
        this.hitBoxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.relativePositionMapper = ComponentMapper.getFor(RelativePositionComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ParentComponent.class,
                TextSelectionComponent.class,
                HitBoxComponent.class,
                PositionComponent.class,
                RelativePositionComponent.class,
                BackgroundColorComponent.class
            ).get()
        );
        this.selectBoxFamily = Family.all(
            ParentComponent.class,
            HitBoxComponent.class,
            PositionComponent.class,
            RelativePositionComponent.class,
            BackgroundColorComponent.class,
            TextHighlightComponent.class,
        ).get();
        this.highLightEntities = engine.getEntitiesFor(selectBoxFamily);
        this.textEntitiesFamily = Family.all(
            TextComponent.class,
            HitBoxComponent.class,
            FocusableComponent.class,
            FocusedComponent.class,
            BitmapFontComponent.class,
            SizeComponent.class,
            AlignmentComponent.class,
            TextSelectionComponent.class
        ).get();
        this.layout = new GlyphLayout();
    }
    
    public void update(float delta) {
        blinker += delta;
        
        for( Entity entity : highLightEntities) {
            getEngine().removeEntity(entity);
        }
        
        for( Entity entity : entities) {
            
            // Get Components of Cursor
            
            TextSelectionComponent textSelectComponent = selectMapper.get(entity);
            HitBoxComponent hitBoxComponent = hitBoxMapper.get(entity);
            RelativePositionComponent relativePositionComponent = relativePositionMapper.get(entity);

            
            
            if(textSelectComponent.textCursorIndex == -1) hitBoxComponent.rectangle.setSize(0,0);
            
            // Get Components of Text ( Parent )
            
            ParentComponent parentComponent = parentMapper.get(entity);
            if( parentComponent == null || parentComponent.parent == null) continue; 
            if( textEntitiesFamily.matches(parentComponent.parent) == false) {
                textSelectComponent.textCursorIndex = -1;
                continue;
            }
            
            TextSelectionComponent parentSelection = selectMapper.get(parentComponent.parent);
            TextComponent textComponent = textMapper.get(parentComponent.parent);
            BitmapFontComponent fontComponent = fontMapper.get(parentComponent.parent);
            AlignmentComponent alignmentComponent = alignMapper.get(parentComponent.parent);
            SizeComponent sizeComponent = sizeMapper.get(parentComponent.parent);

            if(parentSelection.startIndex > parentSelection.endIndex) {
                int temp = parentSelection.startIndex;
                parentSelection.startIndex = parentSelection.endIndex;
                parentSelection.endIndex = temp;
            }
            
            // Adjust index based on parent cursor index
            textSelectComponent.textCursorIndex = parentSelection.textCursorIndex;
            textSelectComponent.startIndex = parentSelection.startIndex;
            textSelectComponent.endIndex = parentSelection.endIndex;
            if(textSelectComponent.textCursorIndex == -1) textSelectComponent.textCursorIndex = textComponent.text.length();
            parentSelection.textCursorIndex = textSelectComponent.textCursorIndex;
            
            if(textComponent.text.length() > 0) { 
            
                // Retrieve Graphical layout representation of text
                layout.setText(fontComponent.font, textComponent.text, Color.BLACK, sizeComponent.width, alignmentComponent.alignment, true);
            
                // Create Textual representation of Graphic Layout of text ( lines and content of lines )
                String[] sections = buildSections(textComponent.text, layout);
            
                // Calculate Line and Row Index of Cursor
                int[] calc = getLineAndIndex(textSelectComponent.textCursorIndex, sections, textComponent.text);
                int sectionIndex = calc[0]; 
                int sectionNumber = calc[1];
           
                // Adjust for "Invisible" non existant lines in graphical layout vs textual layout
                int runNumber = sectionNumber;
                for(int s = 0; s <= sectionNumber; s++) {
                    if(sections[s].trim().equals("")) {
                        runNumber--;
                    }
                }
                if(sections[sectionNumber].trim().equals("")) {sectionIndex = 0;}
            
                // Calculate position of cursor based on row and column of graphical layout
                int[] rp = getRelativePosition(fontComponent.font.getLineHeight(), layout, sizeComponent.height, sectionNumber, sectionIndex, runNumber);
                
                relativePositionComponent.x = rp[0];
                relativePositionComponent.y = rp[1];
                
                if(textSelectComponent.startIndex > -1 && textSelectComponent.endIndex > -1) {
                    
                    int[] n = selectionSections(sections, textSelectComponent.startIndex, textSelectComponent.endIndex, textComponent.text);
                    
                    if(n != null) {
                        
                        for(int i = 0; i < n.length; i+= 3) {
                            int rn = n[i];
                            for(int s = 0; s <= n[i]; s++) {
                                if(sections[s].trim().equals("")) {
                                    rn--;
                                }
                            }
                            if(sections[n[0]].trim().equals("")) {sectionIndex = 0;}
                        
                            int[] srp = getRelativePosition(fontComponent.font.getLineHeight(), layout, sizeComponent.height, n[i], n[i + 1], rn);
                            int[] erp = getRelativePosition(fontComponent.font.getLineHeight(), layout, sizeComponent.height, n[i], n[i + 2], rn);
                        
                            Entity selectEntity = new Entity();
                            selectEntity.add(new PositionComponent());
                            selectEntity.add(new SizeComponent());
                            selectEntity.add(new ParentComponent(parent: parentComponent.parent));
                            selectEntity.add(new HitBoxComponent(rectangle: new Rectangle(1,1, erp[0] - srp[0], fontComponent.font.getLineHeight())));
                            selectEntity.add(new RelativePositionComponent(x: srp[0], y: srp[1], unit: "ppp"));
                            selectEntity.add(new BackgroundColorComponent(color: Color.valueOf("0000ff44")));
                            selectEntity.add(new TextHighlightComponent());
                            getEngine().addEntity(selectEntity);
                        }
                    }
                }
            }
            else {
                relativePositionComponent.x =  0;
                relativePositionComponent.y =  sizeComponent.height - fontComponent.font.getLineHeight();
            }

            relativePositionComponent.z = 1;
            
            // Blinker Timer calculation
            int width = 0;
            if(blinker < 0.75f) {
                width = 1;
            }
            else if ( blinker > 0.75f) {
                width = 0;
            }
            if ( blinker > 1.5f) {
                blinker = 0;
            }
            
            if(textSelectComponent.startIndex != -1) {
                
            }
            
            // Update Cursor Look based on blinker
            int rowHeight = fontComponent.font.getLineHeight();
            hitBoxComponent.rectangle.set(1 , 1 , width, rowHeight);
            
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
    
    private int indexOfFirstNonWhitespaceCharacter(String text) {
        for(int i = 0; i < text.length(); i++) {
            if(!Character.isWhitespace(text.charAt(i))) {
                return i;
            }
        }
        return 0;
    }
    
    private String glyphsToString(Array<BitmapFont.Glyph> glyphs) {
        String s = "";
        for( int i = 0; i < glyphs.size; i++) {
            s += (char) glyphs.get(i).id;
        }
        return s;
    }
    
    private int[] getRelativePosition(float lineHeight, GlyphLayout layout, float height, int sectionNumber, int sectionIndex, int runNumber) {
        int rowHeight = lineHeight;
        int x = 0;
        int y = height - (rowHeight * (sectionNumber + 1)) ;  // top of text
            
        for( int i = 0; i <= sectionIndex & i < layout.runs.get(runNumber).xAdvances.size;  i++) {
            x += layout.runs.get(runNumber).xAdvances.get(i);
        }
        return [x, y];
    }
    
    /**
     * Calculates and returns start and end indexes of each section selection is in
     * Format: int[] {sectionNumber, startIndexOfSection, endIndexOfSection, ...  }
     **/
    private int[] selectionSections(String[] sections, int startIndex, int endIndex, String text) {
       
        
        int[] startInfo = getLineAndIndex(startIndex, sections, text);
        int[] endInfo = getLineAndIndex(endIndex, sections, text);
        
        
        
        int startSection = startInfo[1];
        int endSection = endInfo[1];
        if(startSection == endSection) {
            return [startSection, startInfo[0], endInfo[0]];
        }
        else {
            List<Integer> r = new ArrayList<>();
            
            r.add(startSection);
            r.add(startInfo[0]);
            r.add(sections[startSection].length());
            for(int i = startSection + 1; i <= endSection; i++) {
                if( i == endSection) {
                    r.add(i);
                    r.add(0);
                    r.add(endInfo[0]);
                }
                else {
                    r.add(i);
                    r.add(0);
                    r.add(sections[i].length());
                }
            }
            return r.toArray();
        }
    }
    
    // Calculate Line and Row Index of Cursor
    private int[] getLineAndIndex(int textIndex, String[] sections, String text) {
        int sectionNumber = 0, sectionIndex = textIndex;
        int c = 0;
        for(int i = 0; i < sections.length; i++) {
            if(sectionIndex > sections[i].length()) {
                c += sections[i].length();
                sectionIndex -= sections[i].length();
                if( i < sections.length - 1 ) {
                    int diff = (text.indexOf(sections[i+1], c) - c);
                    sectionIndex -= diff;
                    c += diff;
                }
            }
            else {
                sectionNumber = i;
                break;
            }
        }
        return [sectionIndex, sectionNumber];
    }
}