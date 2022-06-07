package com.pedrocosta.utils.jsonmanager.stream;

import com.google.gson.stream.JsonToken;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.jsonmanager.adapter.JsonReadAdapter;
import com.pedrocosta.utils.output.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class JsonReader {
    private final com.google.gson.stream.JsonReader gsonReader;

    public JsonReader(com.google.gson.stream.JsonReader gsonReader) {
        this.gsonReader = gsonReader;
    }

    @SuppressWarnings("unused")
    public void beginObject() throws IOException {
        gsonReader.beginObject();
    }

    @SuppressWarnings("unused")
    public void endObject() throws IOException {
        gsonReader.endObject();
    }

    @SuppressWarnings("unused")
    public void beginArray() throws IOException {
        gsonReader.beginArray();
    }

    @SuppressWarnings("unused")
    public void endArray() throws IOException {
        gsonReader.endArray();
    }

    public com.google.gson.stream.JsonReader getGson() {
        return gsonReader;
    }

    public <T> T get(String name, Class<T> type) throws IOException {
        return get(name, type, null);
    }

    public <T> T get(String name, Class<T> type, JsonReadAdapter<T> adapter) throws IOException {
        T result = null;
        if (name != null && !name.isEmpty()) {
            if (adapter != null) {
                return adapter.read(this);
            }
            while (gsonReader.hasNext()) {
                JsonToken token = gsonReader.peek();
                if (JsonToken.NAME.equals(token)) {
                    if (gsonReader.nextName().equals(name)) {
                        result = getValue(token, type);
                        break;
                    } else {
                        gsonReader.skipValue();
                    }
                }
            }
        }
        return result;
    }

    public <T> T getObject(String name, JsonUtils jsonUtils, Class<T> type) throws IOException {
        String json = this.get(name, String.class);
        return jsonUtils.fromJson(json, type);
    }

    public <T> T[] getArray(String name, JsonUtils jsonUtils, Class<T> objClass) throws IOException {
        T[] result = null;
        while(gsonReader.hasNext()) {
            if (JsonToken.NAME.equals(gsonReader.peek())) {
                if (gsonReader.nextName().equals(name)) {
                    result = jsonUtils.fromJson(this, (Class<? extends T[]>) Array.newInstance(objClass, 0).getClass());
                    break;
                } else {
                    gsonReader.skipValue();
                }
            }
        }
        return result;
    }

    public <T> Set<T> getSet(String name, JsonUtils jsonUtils, Class<T> objClass) throws IOException {
        List<T> list = getList(name, jsonUtils, objClass);
        if (list == null) return null;
        return new HashSet<>(list);
    }

    public <T> List<T> getList(String name, JsonUtils jsonUtils, Class<T> objClass) throws IOException {
        T[] array = getArray(name, jsonUtils, objClass);
        if (array == null) return null;
        return Arrays.asList(array);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(JsonToken token, Class<T> type) throws IOException {
        T result = null;
        if (JsonToken.NAME.equals(token)) {
            String typeName = type.getSimpleName();
            try {
                switch (typeName) {
                    case "String":
                        result = (T) gsonReader.nextString();
                        break;
                    case "Double":
                        result = (T) Double.valueOf(gsonReader.nextDouble());
                        break;
                    case "Integer":
                        result = (T) Integer.valueOf(gsonReader.nextInt());
                        break;
                    case "Long":
                        result = (T) Long.valueOf(gsonReader.nextLong());
                        break;
                    case "Boolean":
                        result = (T) Boolean.valueOf(gsonReader.nextBoolean());
                        break;
                    default:
                        break;
                }
            } catch (ClassCastException e) {
                Log.error(this, e);
            }
        }
        return result;
    }
}
