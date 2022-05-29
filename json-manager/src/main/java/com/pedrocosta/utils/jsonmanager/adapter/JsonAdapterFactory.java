package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Component;

@Component
public class JsonAdapterFactory extends TypeAdapterFactory {

    @Override
    protected <T> TypeAdapter<T> findAdapter(String name) throws ClassNotFoundException {
        TypeAdapter<T> adapter = null;
        try {
            Class<JsonAdapter<T>> adapterClass = (Class<JsonAdapter<T>>) Class.forName(name);
            if (adapterClass.getConstructors().length > 0) {
                JsonTypeAdapter<T> jsonTypeAdapter = new JsonTypeAdapter<>();
                try {
                    jsonTypeAdapter.setReadAdapter(
                            (JsonReadAdapter) adapterClass.getConstructor(new Class[0]).newInstance());
                } catch (ClassCastException ignored) {
                }
                try {
                    jsonTypeAdapter.setWriteAdapter(
                            (JsonWriteAdapter) adapterClass.getConstructor(new Class[0]).newInstance());
                } catch (ClassCastException ignored) {
                }
                adapter = jsonTypeAdapter;
            }
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }
}
