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
import groovy.transform.CompileStatic;

@CompileStatic
public class HTMLToECSSystem extends EntitySystem {	

    private final ComponentMapper<HTMLComponent> htmlMapper;
    
    
    private ImmutableArray<Entity> entities;

    public HTMLToECSSystem() {
        this.htmlMapper = ComponentMapper.getFor(HTMLComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(HTMLComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            HTMLComponent htmlComponent = htmlMapper.get(entity);
            String html = htmlComponent.html;
            
            if(htmlComponent.parsed == false) {
                Document doc = Jsoup.parse(html);
                Entity root = elementToEntity(entity, doc.body(), true);
                htmlComponent.parsed = true;
            }
        }
    }
    
    public Entity elementToEntity(Entity root, Element el, boolean atRoot) {
        Entity e;
        if(!atRoot) e = new Entity();
        else e = root;
        
        PositionComponent positionComponent = new PositionComponent();
        TagComponent tagComponent = new TagComponent();
        ClassComponent classComponent = new ClassComponent();
        IDComponent idComponent = new IDComponent();
        AttributesComponent attributesComponent = new AttributesComponent();
        TextComponent textComponent = new TextComponent();
        DisplayComponent displayComponent = new DisplayComponent();
        SizeComponent sizeComponent = new SizeComponent();
        HitBoxComponent hitBoxComponent = new HitBoxComponent();
        hitBoxComponent.rectangle = new Rectangle(0,0,0,0);
        BorderComponent borderComponent = new BorderComponent(color: Color.CLEAR);
        
        if(el.hasText()) textComponent.text = el.ownText();
        attributesComponent.attributes = el.attributes();
        classComponent.raw = el.className();
        classComponent.classes = el.classNames();
        idComponent.id = el.id().toLowerCase();
        tagComponent.tag = el.tagName().toLowerCase();
        
        ChildrenComponent childrenComponent = new ChildrenComponent();
        
        for ( Element child : el.children()) {
            Entity childEntity = elementToEntity(root, child, false);
            ParentComponent parentComponent = new ParentComponent();
            parentComponent.parent = e;
            childEntity.add(parentComponent);
            childrenComponent.children.add(childEntity);
        }
        
        for (Entity child : childrenComponent.children) {
            SiblingsComponent sibs = new SiblingsComponent();
            sibs.siblings.addAll(childrenComponent.children);
            child.add(sibs);
        }
        
        PaddingComponent paddingComponent = new PaddingComponent();
        MarginComponent marginComponent = new MarginComponent();
        
        e.add(tagComponent);
        e.add(classComponent);
        e.add(idComponent);
        e.add(attributesComponent);
        e.add(childrenComponent);
        e.add(paddingComponent);
        e.add(marginComponent);
        e.add(hitBoxComponent);
        e.add(borderComponent);
        e.add(displayComponent);
        if(atRoot) e.add(new SiblingsComponent());
        if(!atRoot) e.add(positionComponent);
        if(!atRoot) {e.add(sizeComponent);}
        if(el.hasText() && el.ownText().length() > 0) {e.add(textComponent); e.add(new BitmapFontComponent(font: new BitmapFont(Gdx.files.local("computer/os/assets/userosgui/Lucida Console 12px.fnt")))); e.add(new ColorComponent(color: Color.BLACK));}
        
        if(!atRoot) getEngine().addEntity(e);
        return e;
    }
    
    public void removedFromEngine(Engine engine) {

    }
}