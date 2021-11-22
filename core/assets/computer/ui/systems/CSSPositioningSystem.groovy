package ui.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.math.*;
import ui.components.*;
import os.components.*;
import os.systems.*;

import org.jsoup.*;
import org.jsoup.nodes.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CSSPositioningSystem extends EntitySystem {	

    private final ComponentMapper<ParentComponent> parentMapper;
    private final ComponentMapper<ChildrenComponent> childrenMapper;
    private final ComponentMapper<SiblingsComponent> siblingsMapper;
    private final ComponentMapper<AttributesComponent> attributesMapper;
    private final ComponentMapper<HitBoxComponent> hitBoxMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<TagComponent> tagMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<ClassComponent> classMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<PositionTypeComponent> positionTypeMapper;
    private final ComponentMapper<WidthComponent> widthMapper;
    private final ComponentMapper<HeightComponent> heightMapper;
    private final ComponentMapper<PaddingComponent> paddingMapper;
    private final ComponentMapper<MarginComponent> marginMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<BorderComponent> borderMapper;
    private final ComponentMapper<DisplayComponent> displayMapper;
    
    private ImmutableArray<Entity> entities, htmlElements;
    
    private GlyphLayout layout;
    
    public CSSPositioningSystem() {
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
        this.childrenMapper = ComponentMapper.getFor(ChildrenComponent.class);
        this.siblingsMapper = ComponentMapper.getFor(SiblingsComponent.class);
        this.attributesMapper = ComponentMapper.getFor(AttributesComponent.class);
        this.hitBoxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.tagMapper = ComponentMapper.getFor(TagComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.classMapper = ComponentMapper.getFor(ClassComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.positionTypeMapper = ComponentMapper.getFor(PositionTypeComponent.class);
        this.widthMapper = ComponentMapper.getFor(WidthComponent.class);
        this.heightMapper = ComponentMapper.getFor(HeightComponent.class);
        this.paddingMapper = ComponentMapper.getFor(PaddingComponent.class);
        this.marginMapper = ComponentMapper.getFor(MarginComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.borderMapper = ComponentMapper.getFor(BorderComponent.class);
        this.displayMapper = ComponentMapper.getFor(DisplayComponent.class);
        layout = new GlyphLayout();
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ClassComponent.class, 
                TagComponent.class, 
                IDComponent.class, 
                ParentComponent.class, 
                ChildrenComponent.class,
                SiblingsComponent.class,
                AttributesComponent.class,
                MarginComponent.class, 
                PaddingComponent.class,
                HitBoxComponent.class,
                PositionComponent.class,
                DisplayComponent.class,
                SizeComponent.class,
                CSSComponent.class,
                BorderComponent.class
            ).get()
        );
    }
    
    public void update(float delta) {
        Set<Entity> marked = new HashSet<Entity>();
        for(Entity entity : entities) {
            css(entity);
        }
    }
    
    public void css(Entity entity) {
        ParentComponent parentComponent = parentMapper.get(entity);
        SiblingsComponent siblingsComponent = siblingsMapper.get(entity);
        ChildrenComponent childrenComponent = childrenMapper.get(entity);
        PositionTypeComponent positionTypeComponen = positionTypeMapper.get(entity);
        PositionComponent positionComponent = positionMapper.get(entity);
        WidthComponent widthComponent = widthMapper.get(entity);
        HeightComponent heightComponent = heightMapper.get(entity);
        SizeComponent sizeComponent = sizeMapper.get(entity);
        HitBoxComponent hitBoxComponent = hitBoxMapper.get(entity);
        PaddingComponent paddingComponent = paddingMapper.get(entity);
        MarginComponent marginComponent = marginMapper.get(entity);
        TagComponent tagComponent = tagMapper.get(entity);
        BorderComponent borderComponent = borderMapper.get(entity);
        DisplayComponent displayComponent = displayMapper.get(entity);
        
        
        //Determine Size
        Entity parent = parentComponent.parent;
        SizeComponent parentSize = parent != null ? sizeMapper.get(parent) : null;
        PositionComponent parentPosition = parent != null ? positionMapper.get(parent) : null;
        PaddingComponent parentPadding = parent != null ? paddingMapper.get(parent) : null;
        
        float width, height;
        TextComponent textComponent;
        BitmapFontComponent fontComponent;
        
        if(widthComponent != null) {
            if(widthComponent.type.equals("px")) {
                width = widthComponent.width;
            }
            else if(widthComponent.type.equals("%") && parentSize != null){
                width = widthComponent.width * parentSize.width;
            }
        }
        else {
            if(parentSize != null) {
                width = parentSize.width;
                float numerator = parentSize.width - borderComponent.thickness * 2;
                float denominator = 1;
                for(int i = 1; i < 4; i+=2) {
                    if(paddingComponent.types[i].equals("px")) {
                        numerator -= paddingComponent.padding[i];
                    }
                    else if (paddingComponent.types[i].equals("%")) {
                        denominator += paddingComponent.padding[i];
                    }
                    if(marginComponent.types[i].equals("px")) {
                        numerator -= marginComponent.margin[i];
                    }
                    else if (marginComponent.types[i].equals("%")) {
                        denominator += marginComponent.margin[i];
                    }
                }
                width = numerator / denominator;
            }
        }
        
        
        if(heightComponent != null) {
            if(heightComponent.type.equals("px")) {
                height = heightComponent.height;
            }
            else if(heightComponent.type.equals("%") && parentSize != null){
                height = heightComponent.height * parentSize.height;
                
            }
        }
        else {
            for(int i = 0; i < childrenComponent.children.size(); i++ ) {
                HitBoxComponent childHitBox = hitBoxMapper.get(childrenComponent.children.get(i));
                MarginComponent childMargin = marginMapper.get(childrenComponent.children.get(i));
                if(i != 0 && childMargin.types[0].equals("px")) {
                    height += childMargin.margin[0];
                }
                else if(i != 0 && childMargin.types[0].equals("%") && parentSize != null){
                    height += childMargin.margin[0] * parentSize.height;
                }
                if(i != childrenComponent.children.size() - 1 && childMargin.types[2].equals("px")) {
                    height += childMargin.margin[2];
                }
                else if(i != childrenComponent.children.size() - 1 && childMargin.types[2].equals("%") && parentSize != null){
                    height += childMargin.margin[2] * parentSize.height;
                }
                height += childHitBox.rectangle.getHeight() + childMargin.margin[0];
            }
        }
        
        
        if((textComponent = textMapper.get(entity)) != null && (fontComponent = fontMapper.get(entity)) != null) {
            layout.setText(fontComponent.font, textComponent.text, Color.BLACK, width, Align.left, true);
            if(parentSize != null && parentSize.width < layout.width) {
                layout.setText(fontComponent.font, textComponent.text, Color.BLACK, parentSize.width, Align.left, true);
            }
            height += layout.height;
            width += width == 0 ? layout.width : 0;
            
            layout.reset();
        }
        
        
        
        sizeComponent.width = width;
        sizeComponent.height = height;
        
        //Determine HitBox
        calculateHitBox(paddingComponent, sizeComponent, hitBoxComponent, positionComponent);
        
        //Determine Position
        float x, y, z;
        
        if(parentPosition != null) {
            x += parentPosition.x;
            y += parentPosition.y;
            z = parentPosition.z + 1;
        }

        if(parentSize != null) {
            y += parentSize.height - sizeComponent.height;
            
        }
        
        if(paddingComponent.types[0].equals("%"))        y -= paddingComponent.padding[0] * sizeComponent.width;
        else if(paddingComponent.types[0].equals("px"))  y -= paddingComponent.padding[0];
        if(paddingComponent.types[3].equals("%"))        x += paddingComponent.padding[3] * sizeComponent.width;
        else if(paddingComponent.types[3].equals("px"))  x += paddingComponent.padding[3];
        
        for(Entity sib : siblingsComponent.siblings) {
            if(sib == entity) break;
            HitBoxComponent sibBox = hitBoxMapper.get(sib);
            SizeComponent sibSize = sizeMapper.get(sib);
            MarginComponent sibMargin = marginMapper.get(sib);
            DisplayComponent sibDisplay = displayMapper.get(sib);
            if(!displayComponent.display.equals("inline")) {
                if(!sibDisplay.display.equals("inline")) {
                    y -= sibBox.rectangle.getHeight();
                    if(sibMargin.types[0].equals("%"))        y -= sibMargin.margin[0] * sibSize.width;
                    else if(sibMargin.types[0].equals("px"))  y -= sibMargin.margin[0];
                    if(sibMargin.types[2].equals("%"))        y -= sibMargin.margin[2] * sibSize.width;
                    else if(sibMargin.types[2].equals("px"))  y -= sibMargin.margin[2];
                }
            }
            else {
                x += sibBox.rectangle.getWidth();
            }
        }
        
        if(marginComponent.types[0].equals("%"))        y -= marginComponent.margin[0] * sizeComponent.width;
        else if(marginComponent.types[0].equals("px"))  y -= marginComponent.margin[0];
        if(marginComponent.types[3].equals("%"))        x += marginComponent.margin[3] * sizeComponent.width;
        else if(marginComponent.types[3].equals("px"))  x += marginComponent.margin[3];
        
        positionComponent.x = x;
        positionComponent.y = y;
        positionComponent.z = z;
        
        for(Entity child : childrenComponent.children) {
            css(child);
        }
    }

    //hitbox = padding + size
    //padding can depend on size
    private void calculateHitBox(PaddingComponent padding, SizeComponent size, HitBoxComponent hitbox, PositionComponent position) {
        float top, right, bottom, left;
        float mt, mr, mb, ml;
        
        if(padding.types[0].equals("%"))        top = padding.padding[0] * size.width;
        else if(padding.types[0].equals("px"))  top = padding.padding[0];
        if(padding.types[1].equals("%"))        right = padding.padding[1] * size.width;
        else if(padding.types[1].equals("px"))  right = padding.padding[1];
        if(padding.types[2].equals("%"))        bottom = padding.padding[2] * size.width;
        else if(padding.types[2].equals("px"))  bottom = padding.padding[2];
        if(padding.types[3].equals("%"))        left = padding.padding[3] * size.width;
        else if(padding.types[3].equals("px"))  left = padding.padding[3];
        
        hitbox.rectangle.setSize((float)(right + size.width + left), (float)(top + size.height + bottom));
        hitbox.rectangle.setPosition(-left, -bottom);
    }
    
    public void removedFromEngine(Engine engine) {

    }
}

