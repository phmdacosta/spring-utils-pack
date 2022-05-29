package com.pedrocosta.utils.jsonmanager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.pedrocosta.utils.jsonmanager.adapter.*;
import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class JsonUtils {

    private final TypeAdapterFactory typeAdapterFactory;
    private GsonBuilder builder;
    private boolean useExposedAnnotation;

    public JsonUtils(TypeAdapterFactory typeAdapterFactory) {
        this.typeAdapterFactory = typeAdapterFactory;
    }

    public final String toJson(Object obj) throws NullPointerException, InvalidParameterException {
        return toJson(obj, (JsonWriteAdapter<?>)null,null);
    }

    public  final String toJson(Object obj, TypeAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        return toJson(obj, adapter, null);
    }

    public  final String toJson(Object obj, JsonWriteAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        return toJson(obj, adapter, null);
    }

    public final String toJson(Object obj, String type) throws NullPointerException, InvalidParameterException {
        return toJson(obj, (JsonWriteAdapter<?>)null, type);
    }

    public final String toJson(Object obj, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), adapter, type).toJson(obj);
    }

    public final String toJson(Object obj, JsonWriteAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), null, adapter, type).toJson(obj);
    }

    public final <T> T fromJson(String json, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, (JsonReadAdapter<?>)null, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, adapter, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, JsonReadAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, adapter, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, (JsonReadAdapter<?>)null, type);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(json, classOfT);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, JsonReadAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, null, adapter, type).fromJson(json, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, (JsonReadAdapter<?>)null, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, adapter, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, (JsonReadAdapter<?>)null, type);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(reader, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, JsonReadAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, null, adapter, type).fromJson(reader, classOfT);
    }

    public JsonUtils setUseExposedAnnotation(boolean useExposedAnnotation) {
        this.useExposedAnnotation = useExposedAnnotation;
        return this;
    }

    private <T> Gson createGson(Class<T> classOfT, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, null, type);
    }

    @SuppressWarnings("unchecked")
    private <T> Gson createGson(Class<T> classOfT, TypeAdapter<?> typeAdapter, JsonAdapter<?> jsonAdapter, String type) throws NullPointerException, InvalidParameterException {
        if (builder == null) {
            builder = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }

        if (useExposedAnnotation) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }

        Gson gson = builder.create();

        if (jsonAdapter != null) {
            JsonTypeAdapter<?> jsonTypeAdapter = new JsonTypeAdapter<>();
            try {
                jsonTypeAdapter.setReadAdapter((JsonReadAdapter) jsonAdapter);
            } catch (ClassCastException ignored) {
            }
            try {
                jsonTypeAdapter.setWriteAdapter((JsonWriteAdapter) jsonAdapter);
            } catch (ClassCastException ignored) {
            }
            typeAdapter = jsonTypeAdapter;
        }

        if (typeAdapter == null && typeAdapterFactory != null) {
            typeAdapter = typeAdapterFactory.create(classOfT, type);
        }

        if (typeAdapter != null) {
            gson = builder.registerTypeAdapter(classOfT, typeAdapter).create();
        } else {
            Log.warn(this, String.format(
                    "Could not find adapter of %s for api type %s.",
                    classOfT.getSimpleName(), type));
        }

        return gson;
    }
}
