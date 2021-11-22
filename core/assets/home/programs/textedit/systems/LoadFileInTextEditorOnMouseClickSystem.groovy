
package textedit.systems;

import com.janfic.useride.kernel.components.*;
import os.components.*;
import os.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.gdx.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import htmlTester.components.*;
import ui.components.*;
import ui.systems.*;
import textedit.components.*;

import java.util.Arrays;

public class LoadFileInTextEditorOnMouseClickSystem extends EntitySystem {

    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<TextSelectionComponent> selectMapper;


    private ImmutableArray<Entity> pathEntity, textEditEntity, entities;
    
    public LoadFileInTextEditorOnMouseClickSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.selectMapper = ComponentMapper.getFor(TextSelectionComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.pathEntity = engine.getEntitiesFor(
            Family.all(TextComponent.class, PathComponent.class).get()
        );
        this.textEditEntity = engine.getEntitiesFor(
            Family.all(TextComponent.class, TextEditComponent.class).get()
        );
        this.entities = engine.getEntitiesFor(
            Family.all(LoadFileOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    @Override
    public void update(float delta) {
        if(pathEntity.size() < 1) return;
        if(textEditEntity.size() < 1) return;
        Entity path = pathEntity.first();
        Entity textEdit = textEditEntity.first();
        for(Entity entity : entities) {
            TextComponent pathText = textMapper.get(path);
            TextComponent textEditText = textMapper.get(textEdit);
            TextSelectionComponent select = selectMapper.get(textEdit);
            
            FileHandle file = Gdx.files.local(pathText.text);
            if(file.exists() && file.isDirectory() == false) {
                textEditText.text = file.readString();
                select.startIndex = -1;
                select.endIndex = -1;
                select.textCursorIndex = -1;
            }
            
            entity.remove(MouseClickEventComponent.class);
        }
    }
}