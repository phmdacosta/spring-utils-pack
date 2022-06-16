package com.pedrocosta.springutils.jsonmanager.stream;

import com.pedrocosta.springutils.jsonmanager.JsonUtils;

import java.io.IOException;

/**
 * Handle JSON serialization.
 *
 * @author Pedro H. M. da Costa
 * @since 1.0
 */
public class JsonWriter {
    private final com.google.gson.stream.JsonWriter gsonWriter;

    /**
     * Constructor.
     *
     * @param gsonWriter    {@link com.google.gson.stream.JsonWriter} object
     */
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

    public com.google.gson.stream.JsonWriter gsonWriter() {
        return gsonWriter;
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, String value) throws IOException {
        if (value != null && !value.isEmpty()) {
            setName(name);
            gsonWriter.value(value);
        }
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, int value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, double value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, boolean value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, long value) throws IOException {
        setName(name);
        gsonWriter.value(value);
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Value of the field
     */
    public void add(String name, Number value) throws IOException {
        if (value != null) {
            setName(name);
            gsonWriter.value(value);
        }
    }

    /**
     * Add value to json.
     * @param name      Name of json field
     * @param jsonUtils {@link JsonUtils} to serialize value
     */
    public <T> void addJson(String name, T obj, JsonUtils jsonUtils) throws IOException {
        addJson(name, jsonUtils.toJson(obj));
    }

    /**
     * Add value to json.
     * @param name  Name of json field
     * @param value Serialized json object
     */
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
