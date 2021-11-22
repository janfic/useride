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


public class CSSToECSSystem extends EntitySystem {	

    private final ComponentMapper<CSSComponent> cssMapper;
    private final ComponentMapper<TagComponent> tagMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<ClassComponent> classMapper;
    
    private ImmutableArray<Entity> entities, htmlElements;

    final String regex = "(?:[\\.#]?\\w+\\s*,?\\s*)+\\{(?:\\s*\\w+:\\s*\\S+;)*(?:\\s*[-\\w]+:(?:\\s*[^\\s\\}])+;?)?\\s*\\}";
    final String valuesRegex = "\\s+";
    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Pattern valuesPattern = Pattern.compile(valuesRegex, Pattern.MULTILINE);
    
    public CSSToECSSystem() {
        this.cssMapper = ComponentMapper.getFor(CSSComponent.class);
        this.tagMapper = ComponentMapper.getFor(TagComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.classMapper = ComponentMapper.getFor(ClassComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(CSSComponent.class).get()
        );
        this.htmlElements = engine.getEntitiesFor(
            Family.all(
                ClassComponent.class, 
                TagComponent.class, 
                IDComponent.class, 
                ParentComponent.class, 
                ChildrenComponent.class, 
                AttributesComponent.class,
                MarginComponent.class, 
                PaddingComponent.class)
            .get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            CSSComponent cssComponent = cssMapper.get(entity);
            String css = cssComponent.css;
            
            if(cssComponent.parsed == false) {
                ArrayList<String> orderedKeys = new ArrayList<String>();
                Map<String, String> rules = parse(css, orderedKeys);
                cssComponent.parsed = true;
                
                for(String key : orderedKeys) {
                    String rule = rules.get(key);
                    for(Entity element : htmlElements) {
                        TagComponent tagComponent = tagMapper.get(element);
                        ClassComponent classComponent = classMapper.get(element);
                        IDComponent idComponent = idMapper.get(element);
                        
                        boolean match = true;
                        if(key.startsWith(".")) {
                            match = match && classComponent.classes.contains(key.substring(1).trim());
                        }
                        else if(key.startsWith("#")) {
                            match = match && idComponent.id.equals(key.substring(1).trim());
                        }
                        else {
                            match = match && tagComponent.tag.equals(key.trim());
                        }
                        if(match) generateComponents(rule, element);
                    }
                }
            }
        }
    }
    
    public void generateComponents(String rule, Entity entity) {
        String[] properties = rule.split(";");
        for(String property : properties) {
            String name = property.split(":")[0].trim();
            String[] values = valuesPattern.split(property.split(":")[1].trim(), 0);
            switch(name.toLowerCase()) {
            case "color":
                ColorComponent colorComponent = new ColorComponent();
                if(values[0].contains("#")) {
                    colorComponent.color = Color.valueOf(values[0].trim());
                }
                else {
                    colorComponent.color = Colors.get(values[0].trim().toUpperCase());
                }
                entity.add(colorComponent);
                break;
            case "background-color":
                BackgroundColorComponent backgroundColorComponent = new BackgroundColorComponent();
                if(values[0].contains("#")) {
                    backgroundColorComponent.color = Color.valueOf(values[0].trim());
                }
                else {
                    backgroundColorComponent.color = Colors.get(values[0].trim().toUpperCase());
                }
                entity.add(backgroundColorComponent);
                break;
            case "height":
                HeightComponent heightComponent = new HeightComponent();
                if(values[0].contains("px")){ 
                    heightComponent.height = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    heightComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    heightComponent.height = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    heightComponent.type = "%";
                }
                entity.add(heightComponent);
                break;
            case "width":
                WidthComponent widthComponent = new WidthComponent();
                if(values[0].contains("px")){ 
                    widthComponent.width = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    widthComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    widthComponent.width = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    widthComponent.type = "%";
                }
                entity.add(widthComponent);
                break;
            case "margin":
                MarginComponent margin = new MarginComponent();
                String[] types = ["px", "%"];
                for(int i = 0; i < values.length; i++) {
                    for(int j = 0; j < types.length; j++) {
                        if(values[i].contains(types[j])) {
                            margin.margin[i] = Float.parseFloat(values[i].trim().substring(0, values[i].indexOf(types[j])));
                            margin.types[i] = types[j];
                            if(values[i].contains("%")) {
                                margin.margin[i] *= 0.01;
                            }
                            break;
                        }
                    }
                }
                
                if(values.length == 1) {
                    margin.margin[3] = margin.margin[2] = margin.margin[1] = margin.margin[0];
                    margin.types[3] = margin.types[2] = margin.types[1] = margin.types[0];
                }
                else if(values.length == 2) {
                    margin.margin[2] = margin.margin[0];
                    margin.margin[3] = margin.margin[1];
                }
                else if(values.length == 3) {
                    margin.margin[3] = margin.margin[1];
                }
                entity.add(margin);
                break;
            case "padding":
                PaddingComponent padding = new PaddingComponent();
                String[] types = ["px", "%"];
                for(int i = 0; i < values.length; i++) {
                    for(int j = 0; j < types.length; j++) {
                        if(values[i].contains(types[j])) {
                            padding.padding[i] = Float.parseFloat(values[i].trim().substring(0, values[i].indexOf(types[j])));
                            padding.types[i] = types[j];
                            if(values[i].contains("%")) {
                                padding.padding[i] *= 0.01;
                            }
                            break;
                        }
                    }
                }
                
                if(values.length == 1) {
                    padding.padding[3] = padding.padding[2] = padding.padding[1] = padding.padding[0];
                    padding.types[3] = padding.types[2] = padding.types[1] = padding.types[0];
                }
                else if(values.length == 2) {
                    padding.padding[2] = padding.padding[0];
                    padding.padding[3] = padding.padding[1];
                }
                else if(values.length == 3) {
                    padding.padding[3] = padding.padding[1];
                }
                entity.add(padding);
                break;
            case "position":
                PositionTypeComponent positionTypeComponent = new PositionTypeComponent();
                positionTypeComponent.type = values[0];
                entity.add(positionTypeComponent)
                break;
            case "top":
                TopComponent topComponent = new TopComponent();
                if(values[0].contains("px")){ 
                    topComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    topComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    topComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    topComponent.type = "%";
                }
                entity.add(topComponent);
                break;
            case "bottom":
                BottomComponent bottomComponent = new BottomComponent();
                if(values[0].contains("px")){ 
                    bottomComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    bottomComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    bottomComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    bottomComponent.type = "%";
                }
                entity.add(bottomComponent);
                break;
            case "left":
                LeftComponent leftComponent = new LeftComponent();
                if(values[0].contains("px")){ 
                    leftComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    leftComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    leftComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    leftComponent.type = "%";
                }
                entity.add(leftComponent);
                break;
            case "right":
                RightComponent rightComponent = new RightComponent();
                if(values[0].contains("px")){ 
                    rightComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    rightComponent.type = "px";
                }
                else if (values[0].contains("%")){
                    rightComponent.value = Float.parseFloat(values[0].substring(0, values[0].indexOf("%"))) * 0.01f;
                    rightComponent.type = "%";
                }
                entity.add(rightComponent);
                break;
            case "border-radius":
                BorderComponent border = entity.getComponent(BorderComponent.class);
                if(values[0].contains("px")) {
                    border.radius = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    border.radiusType = "px";
                }
                break;
            case "border-color":
                BorderComponent border = entity.getComponent(BorderComponent.class);
                if(values[0].contains("#")) {
                    border.color = Color.valueOf(values[0].trim());
                }
                else {
                    border.color = Colors.get(values[0].trim().toUpperCase());
                }
                break;
            case "border-width":
                BorderComponent border = entity.getComponent(BorderComponent.class);
                if(values[0].contains("px")) {
                    border.thickness = Float.parseFloat(values[0].substring(0, values[0].indexOf("px")));
                    border.thicknessType = "px";
                }
                break;
            case "display":
                DisplayComponent display = entity.getComponent(DisplayComponent.class);
                display.display = values[0];
                System.out.println(display.display)
                break;
            }
        }
    }
    
    public Map<String, String> parse(String string, List<String> orderedKeys) {
        Map<String, String> rules = new HashMap<String, String>();
        
        final Matcher matcher = pattern.matcher(string);
        ArrayList<String> matches = new ArrayList<String>();
        while(matcher.find()) {
            matches.add(matcher.group(0));
        }
        
        for(String match : matches) {
            String quantifiers = match.substring(0, match.indexOf("{")).trim();
            String rule = match.substring(match.indexOf("{") + 1, match.length() - 1).trim();
            String[] keys = quantifiers.split(",");
            
            for(String key : keys) {
                rules.put(key.trim(), rule);
                orderedKeys.add(key.trim());
            }
        }
        
        return rules;
    }
    
    public void removedFromEngine(Engine engine) {

    }
}
