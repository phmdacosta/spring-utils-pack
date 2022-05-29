package com.pedrocosta.utils.jsonmanager.stream;

import java.io.IOException;
import java.io.Writer;

public class JsonWriter {
    private final com.google.gson.stream.JsonWriter gsonWriter;

    public JsonWriter(com.google.gson.stream.JsonWriter gsonWriter) {
        this.gsonWriter = gsonWriter;
    }

    public void beginObject() throws IOException {
        gsonWriter.beginObject();
    }

    public void add(String name, String value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void add(String name, int value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void add(String name, double value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void add(String name, boolean value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void add(String name, long value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void add(String name, Number value) throws IOException {
        gsonWriter.name(name);
        gsonWriter.value(value);
    }

    public void endObject() throws IOException {
        gsonWriter.endObject();
    }
}
