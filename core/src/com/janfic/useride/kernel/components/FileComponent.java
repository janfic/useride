package com.janfic.useride.kernel.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class FileComponent implements Component, Serializable {

    public FileHandle file;

    @Override
    public void write(Json json) {
        json.writeField(file.path(), "path");
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        file = Gdx.files.absolute(jsonData.child().asString());
    }
}