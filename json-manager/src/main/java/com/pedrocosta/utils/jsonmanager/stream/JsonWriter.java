package com.pedrocosta.utils.jsonmanager.stream;

import com.pedrocosta.utils.jsonmanager.JsonUtils;

import java.io.IOException;

public class JsonWriter {
    private final com.google.gson.stream.JsonWriter gsonWriter;

    public JsonWriter(com.google.gson.stream.JsonWriter gsonWriter) {
        this.gsonWriter = gsonWriter;
    }

    @SuppressWarnings("unused")
    public void beginObject() throws IOException {
        gsonWriter.beginObject();
    }

    @SuppressWarnings("unused")
    public void endObject() throws IOException {
        gsonWriter.endObject();
    }

    @SuppressWarnings("unused")
    public void beginArray() throws IOException {
        gsonWriter.beginArray();
    }

    @SuppressWarnings("unused")
    public void endArray() throws IOException {
        gsonWriter.endArray();
    }

    public com.google.gson.stream.JsonWriter getGson() throws IOException {
        return gsonWriter;
    }

    public void add(String name, String value) throws IOException {
        if (value != null && !value.isEmpty()) {
            setName(name);
            gsonWriter.value(value);
        }
    }

    public void add(String name, int value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    public void add(String name, double value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    public void add(String name, boolean value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    public void add(String name, long value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    public void add(String name, Number value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    public <T> void addJson(String name, T obj, JsonUtils jsonUtils) throws IOException {
        addJson(name, jsonUtils.toJson(obj));
    }

    public void addJson(String name, String value) throws IOException {
        if (value != null && !value.isEmpty()) {
            setName(name);
            gsonWriter.jsonValue(value);
        }
    }

    private void setName(String name) throws IOException {
        if (name != null) {
            gsonWriter.name(name);
        }
    }
}
