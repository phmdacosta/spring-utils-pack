package com.pedrocosta.springutils.jsonmanager.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public abstract class JsonObjectSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private JsonObject jsonObject;

    public abstract JsonObject serialize(T obj, Type type);

    public abstract T deserialize(JsonObject jsonObject, Type type) throws JsonParseException;

    @Override
    public JsonElement serialize(T obj, Type type, JsonSerializationContext jsonSerializationContext) {
        initJson();
        return serialize(obj, type);
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonObject()) {
            throw new JsonParseException("Not a JSON Object: " + jsonElement);
        }
        return deserialize(jsonElement.getAsJsonObject(), type);
    }

    public void addField(String name, String value) {
        jsonObject.addProperty(name, value);
    }

    public void addField(String name, Number value) {
        jsonObject.addProperty(name, value);
    }

    public void addField(String name, Boolean value) {
        jsonObject.addProperty(name, value);
    }

    public void addField(String name, Character value) {
        jsonObject.addProperty(name, value);
    }

    private void initJson() {
        this.jsonObject = new JsonObject();
    }
}
