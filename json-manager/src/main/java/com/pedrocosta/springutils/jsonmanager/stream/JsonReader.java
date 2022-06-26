package com.pedrocosta.springutils.jsonmanager.stream;

import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.pedrocosta.springutils.NumberUtils;
import com.pedrocosta.springutils.jsonmanager.JsonUtils;
import com.pedrocosta.springutils.WrapperType;
import com.pedrocosta.springutils.output.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Handle JSON deserialization.
 *
 * @author Pedro H. M. da Costa
 * @since 1.0
 */
public class JsonReader {
    private final com.google.gson.stream.JsonReader gsonReader;
    private Map<Object, Object> cache = new HashMap<>();

    /**
     * Constructor.
     * <br>
     * It does a preload of json values into a Map.
     *
     * @param gsonReader    {@link com.google.gson.stream.JsonReader} object
     */
    public JsonReader(com.google.gson.stream.JsonReader gsonReader) {
        this.gsonReader = gsonReader;
        try {
            cache = load(this.gsonReader);
        } catch (IOException e) {
            Log.error(this, e);
        }
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

    /**
     * Get original gson reader object.
     * @return {@link com.google.gson.stream.JsonReader} object
     */
    public com.google.gson.stream.JsonReader gsonReader() {
        return gsonReader;
    }

    /**
     * Get field value from json.
     *
     * @param <T>       Type of returned object
     * @param name      Name of json field
     * @param objClass  Class of return object
     * @return  Object of {@link T}
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name, Class<T> objClass) {
        T result = initValue(objClass);
        Object value = cache.get(name);
        if (value != null) {
            try {
                if (NumberUtils.isNumberClass(objClass))
                    result = (T) NumberUtils.valueOf(String.valueOf(value),
                            (Class<? extends Number>)objClass);
                else
                    result = objClass.cast(cache.get(name));
            } catch (ClassCastException ignored) {}
        }
        return result;
    }

    /**
     * Get object from json.
     *
     * @param <T>       Type of returned object
     * @param name      Name of json field
     * @param jsonUtils {@link JsonUtils}
     * @param objClass  Class of return object
     * @return  Object of {@link T}
     */
    public <T> T getObject(String name, JsonUtils jsonUtils, Class<T> objClass) {
        String json = this.get(name, String.class);
        return jsonUtils.fromJson(json, objClass);
    }

    /**
     * Get array from json.
     *
     * @param <T>       Type of returned object
     * @param name      Name of json field
     * @param jsonUtils {@link JsonUtils}
     * @param objClass  Class of return object
     * @return  Array of objects
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getArray(String name, JsonUtils jsonUtils, Class<T> objClass) {
        String value = this.get(name, String.class);
        return jsonUtils.fromJson(value,
                (Class<? extends T[]>) Array.newInstance(objClass, 0).getClass());
    }

    /**
     * Get set from json.
     *
     * @param <T>       Type of returned object
     * @param name      Name of json field
     * @param jsonUtils {@link JsonUtils}
     * @param objClass  Class of return object
     * @return  Set of objects
     */
    public <T> Set<T> getSet(String name, JsonUtils jsonUtils, Class<T> objClass) {
        List<T> list = getList(name, jsonUtils, objClass);
        if (list == null) return null;
        return new HashSet<>(list);
    }

    /**
     * Get list from json.
     *
     * @param <T>       Type of returned object
     * @param name      Name of json field
     * @param jsonUtils {@link JsonUtils}
     * @param objClass  Class of return object
     * @return  List of objects
     */
    public <T> List<T> getList(String name, JsonUtils jsonUtils, Class<T> objClass) {
        T[] array = getArray(name, jsonUtils, objClass);
        if (array == null) return null;
        return Arrays.asList(array);
    }

    /**
     * Get an initial value if object is primitive.
     *
     * @param <T> Object type
     * @param objClass  Class of object to init.
     * @return  Initial value for type
     */
    private <T> T initValue(Class<T> objClass) {
        T result = null;
        if (WrapperType.is(objClass) && objClass.isPrimitive()) {
            result = WrapperType.init(objClass);
        }
        return result;
    }

    private Object getValue(JsonToken token) throws IOException {
        Object result = null;
        try {
            switch (token) {
                case NUMBER:
                case STRING:
                    result = gsonReader.nextString();
                    break;
                case BOOLEAN:
                    result = gsonReader.nextBoolean();
                    break;
                default:
            }
        } catch (ClassCastException e) {
            Log.error(this, e);
        }
        return result;
    }

    private Map<Object, Object> load(com.google.gson.stream.JsonReader reader) throws IOException {
        Map<Object, Object> map = new HashMap<>();
        int i = 0;
        Object name = null;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            switch (token) {
                case NAME:
                    name = reader.nextName();
                    break;
                case BEGIN_OBJECT:
                    if (name == null) {
                        name = i++;
                    }
                    reader.beginObject();
                    map.put(name, loadJsonObjectAsString(reader));
                    reader.endObject();
                    break;
                case BEGIN_ARRAY:
                    if (name == null) {
                        name = i++;
                    }
                    reader.beginArray();
                    map.put(name, loadJsonArrayAsString(reader));
                    reader.endArray();
                    break;
                default:
                    if (name == null) {
                        name = i++;
                    }
                    Object value = null;
                    boolean got = false;
                    while(!got) {
                        try {
                            value = getValue(token);
                            got = true;
                        } catch (MalformedJsonException ignored){}
                    }
                    if (value != null) {
                        map.put(name, value);
                        name = null;
                    }
            }
        }
        return map;
    }

    private String loadJsonObjectAsString(com.google.gson.stream.JsonReader reader) throws IOException {
        return "{" + loadAsString(reader) + "}";
    }

    private String loadJsonArrayAsString(com.google.gson.stream.JsonReader reader) throws IOException {
        return "[" + loadAsString(reader) + "]";
    }

    private String loadAsString(com.google.gson.stream.JsonReader reader) throws IOException {
        Map<String, String> map = new TreeMap<>();
        String name = null;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            switch (token) {
                case NAME:
                    name = "\"" + reader.nextName() + "\"";
                    break;
                case BEGIN_OBJECT:
                    StringBuilder objStr = new StringBuilder();
                    objStr.append("{");
                    reader.beginObject();
                    objStr.append(loadAsString(reader));
                    reader.endObject();
                    objStr.append("}");
                    if (name == null)
                        name = objStr.toString();
                    map.put(name, objStr.toString());
                    name = null;
                    break;
                case BEGIN_ARRAY:
                    StringBuilder arrayStr = new StringBuilder();
                    arrayStr.append("[");
                    reader.beginArray();
                    arrayStr.append(loadAsString(reader));
                    reader.endArray();
                    arrayStr.append("]");
                    if (name == null)
                        name = arrayStr.toString();
                    map.put(name, arrayStr.toString());
                    name = null;
                    break;
                default:
                    Object value = null;
                    boolean got = false;
                    while(!got) {
                        try {
                            value = getValue(token);
                            got = true;
                        } catch (MalformedJsonException ignored){}
                    }
                    if (value != null) {
                        String valueStr = String.valueOf(value);
                        if (value instanceof String) {
                            valueStr = "\"" + value + "\"";
                        }
                        if (name == null)
                            name = valueStr;
                        map.put(name, valueStr);
                        name = null;
                    }
            }
        }

        StringBuilder result = new StringBuilder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        int i = 0;
        for (Map.Entry<String,String> entry : entries) {
            if (i > 0)
                result.append(",");
            // If key and value are equal, means that we have no key
            if (!entry.getKey().equals(entry.getValue())) {
                result.append(entry.getKey());
                result.append(":");
            }
            result.append(entry.getValue());
            i++;
        }
        return result.toString();
    }
}
