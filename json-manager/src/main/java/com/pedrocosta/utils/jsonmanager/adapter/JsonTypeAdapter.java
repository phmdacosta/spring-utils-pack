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
 * Custom type adapter from Gson.
 * It does all the repetitive work and let its child handle
 * just writing and reading of object's fields.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class JsonTypeAdapter<T> extends TypeAdapter<T> {
    private JsonUtils jsonUtils;
    private JsonReadAdapter<T> readAdapter;
    private JsonWriteAdapter<T> writeAdapter;

    public JsonReadAdapter<T> getReadAdapter() {
        return readAdapter;
    }

    public void setReadAdapter(JsonReadAdapter<T> readAdapter) {
        this.readAdapter = readAdapter;
    }

    public JsonWriteAdapter<T> getWriteAdapter() {
        return writeAdapter;
    }

    public void setWriteAdapter(JsonWriteAdapter<T> writeAdapter) {
        this.writeAdapter = writeAdapter;
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

    protected void writeJson(JsonWriter writer, T obj) throws IOException, NotSupportedException {
        if (writeAdapter != null)
            writeAdapter.write(new com.pedrocosta.utils.jsonmanager.stream.JsonWriter(writer), obj);
    }

    protected T readJson(JsonReader reader) throws IOException, NotSupportedException {
        if (readAdapter == null) return null;
        return readAdapter.read(new com.pedrocosta.utils.jsonmanager.stream.JsonReader(reader));
    }
}
