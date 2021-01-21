package terminal.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.files.FileHandle;
import terminal.components.*;
import os.components.*;
import os.systems.*;

@groovy.transform.CompileStatic
public class RunCommandOnKeyEnterSystem extends EntitySystem {	

    private final ComponentMapper<CommandComponent> commandMapper;
    private final ComponentMapper<KeyInputComponent> keyMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    
    private ImmutableArray<Entity> entities;
    
    public RunCommandOnKeyEnterSystem() {
        this.commandMapper = ComponentMapper.getFor(CommandComponent.class);
        this.keyMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
    }    
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                CommandComponent.class,
                KeyInputComponent.class,
                RunCommandOnKeyEnterComponent.class,
                FileComponent.class
            ).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            CommandComponent command = commandMapper.get(entity);
            KeyInputComponent keyInput = keyMapper.get(entity);
            FileComponent dir = fileMapper.get(entity);
            
            if(keyInput.keyTyped == 13 && command.text.length() > 0) {
                String[] commands = command.text.split("\\s*\\|\\s*");
                System.out.println(Arrays.toString(commands));
                ProgramOutputComponent previousOutput = null;
                for(int i = 0; i < commands.length; i++) {
                    String[] text = commands[i].split(" ");
                    if(text.length > 0) {
                        String program = text[0].trim();
                        String input = null;
                        if(text.length > 1) {
                            input = commands[i].substring(commands[i].indexOf(program) + program.length() + 1);
                        }
                    
                        FileHandle programFolder = Gdx.files.local("home/programs/" + program);
                        
                        if(!program.equals("os") && programFolder.exists()) {
                            Entity injectionEntity = new Entity();
                        
                            ProgramInputComponent programInput = new ProgramInputComponent();
                            if(previousOutput != null) programInput.input = previousOutput.output;
                            programInput.input.add(dir);
                            if(input != null) programInput.input.add(input);
                            
                            ProgramOutputComponent programOutput = new ProgramOutputComponent();
                            previousOutput = programOutput;
                            
                            injectionEntity.add(programInput);
                            injectionEntity.add(programOutput);
                            
                            Entity runCommandAttempt = new Entity();
                        
                            ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent(name: program);
                            ProgramEntityInjectionComponent injection = new ProgramEntityInjectionComponent();
                            injection.entities.add(injectionEntity);
                            FileComponent fileComponent = new FileComponent(file: programFolder);
                        
                            runCommandAttempt.add(startRequest);
                            runCommandAttempt.add(injection);
                            runCommandAttempt.add(fileComponent);
                            runCommandAttempt.add(programOutput);
                            runCommandAttempt.add(programInput);
                            if(i != commands.length - 1) runCommandAttempt.add(new PipedCommandComponent());
                            
                            this.getEngine().addEntity(runCommandAttempt);
                        }
                    }
                }
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}
