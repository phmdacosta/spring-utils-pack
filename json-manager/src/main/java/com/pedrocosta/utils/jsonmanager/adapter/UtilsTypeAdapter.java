package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.output.Log;

import java.io.IOException;

public abstract class UtilsTypeAdapter<T> extends TypeAdapter<T> implements JsonWriteAdapter<T>, JsonReadAdapter<T> {

    private JsonUtils jsonUtils;

    public JsonUtils getJsonUtils() {
        if (jsonUtils == null) {
            UtilsTypeAdapterFactory adapterFactory = new UtilsTypeAdapterFactory()
                    .setPackageUri(this.getClass().getPackage().getName());
            jsonUtils = new JsonUtils().setTypeAdapterFactory(adapterFactory);
        }
        return jsonUtils;
    }

    public void setJsonUtils(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    @Override
    public void write(JsonWriter writer, T t) throws IOException {
        if (t == null) {
            writer.nullValue();
        } else {
            begin(writer);
            try {
                writeJson(writer, t);
            } catch (NotSupportedException e) {
                Log.error(this, e);
            } finally {
                end(writer);
            }
        }
    }

    @Override
    public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        begin(reader);
        T obj = null;
        try {
            obj = readJson(reader);
        } catch (NotSupportedException e) {
            Log.error(this, e);
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        } finally {
            end(reader);
        }

        return obj;
    }

    protected void begin(JsonReader reader) throws IOException {
        reader.beginObject();
    }

    protected void end(JsonReader reader) throws IOException {
        skipToEnd(reader);
        reader.endObject();
    }

    protected void begin(JsonWriter writer) throws IOException {
        writer.beginObject();
    }

    protected void end(JsonWriter writer) throws IOException {
        writer.endObject();
    }

    protected void skipToEnd(JsonReader reader) throws IOException {
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (!JsonToken.END_OBJECT.equals(token)) {
                reader.skipValue();
            }
        }
    }

    protected void writeJson(JsonWriter writer, T obj) throws IOException, NotSupportedException {
        this.write(
                new com.pedrocosta.utils.jsonmanager.stream.JsonWriter(writer), obj);
    }

    protected T readJson(JsonReader reader) throws IOException, NotSupportedException {
        return this.read(
                new com.pedrocosta.utils.jsonmanager.stream.JsonReader(reader));
    }
}
