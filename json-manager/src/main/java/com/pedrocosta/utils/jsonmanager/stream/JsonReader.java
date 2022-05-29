package com.pedrocosta.utils.jsonmanager.stream;

import com.google.gson.stream.JsonToken;
import com.pedrocosta.utils.output.Log;

import java.io.IOException;

public class JsonReader {
    private final com.google.gson.stream.JsonReader gsonReader;

    public JsonReader(com.google.gson.stream.JsonReader gsonReader) {
        this.gsonReader = gsonReader;
    }

    public void beginObject() throws IOException {
        gsonReader.beginObject();
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String name, Class<E> type) throws IOException {
        E result = null;
        if (name != null && !name.isEmpty()) {
            if (gsonReader.hasNext()) {
                JsonToken token = gsonReader.peek();
                if (JsonToken.NAME.equals(token) && name.equals(gsonReader.nextName())) {
                    String typeName = type.getSimpleName();
                    try {
                        switch (typeName) {
                            case "String":
                                result = (E) gsonReader.nextString();
                                break;
                            case "Double":
                                result = (E) Double.valueOf(gsonReader.nextDouble());
                                break;
                            case "Integer":
                                result = (E) Integer.valueOf(gsonReader.nextInt());
                                break;
                            case "Long":
                                result = (E) Long.valueOf(gsonReader.nextLong());
                                break;
                            case "Boolean":
                                result = (E) Boolean.valueOf(gsonReader.nextBoolean());
                                break;
                            default:
                                break;
                        }
                    } catch (ClassCastException e) {
                        Log.error(this, e);
                    }
                }
            }
        }
        return result;
    }

    public void endObject() throws IOException {
        gsonReader.endObject();
    }
}
