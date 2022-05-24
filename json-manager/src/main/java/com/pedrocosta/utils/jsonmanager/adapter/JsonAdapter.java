package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.output.Log;

import java.io.IOException;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public abstract class JsonAdapter<T> extends TypeAdapter<T> {
    private final JsonUtils jsonUtils;

    public JsonAdapter() {
        jsonUtils = new JsonUtils(new AdapterFactory());
    }

    public JsonUtils getGsonUtils() {
        return jsonUtils;
    }

    @Override
    public void write(JsonWriter writer, T t) throws IOException {
        beginWriter(writer);
        try {
            writeJson(writer, t);
        } catch (NotSupportedException e) {
            Log.error(this, e);
        } finally {
            endWriter(writer);
        }
    }

    @Override
    public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        beginReader(reader);
        T obj = null;
        try {
            obj = readJson(reader);
        } catch (NotSupportedException e) {
            Log.error(this, e);
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        } finally {
            endReader(reader);
        }

        return obj;
    }

    protected void beginWriter(JsonWriter writer) throws IOException {
        writer.beginObject();
    }

    protected void endWriter(JsonWriter writer) throws IOException {
        writer.endObject();
    }

    protected void beginReader(JsonReader reader) throws IOException {
        reader.beginObject();
    }

    protected void endReader(JsonReader reader) throws IOException {
        reader.endObject();
    }

    protected abstract void writeJson(JsonWriter writer, T obj) throws IOException, NotSupportedException;

    protected abstract T readJson(JsonReader reader) throws IOException, NotSupportedException;

//    protected abstract T readEach(String name, T t) throws IOException, NotSupportedException;
}
