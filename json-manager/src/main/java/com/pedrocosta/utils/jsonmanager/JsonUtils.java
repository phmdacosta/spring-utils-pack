package com.pedrocosta.utils.jsonmanager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.utils.jsonmanager.stream.JsonReader;
import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

@Component
public class JsonUtils {

    private UtilsTypeAdapterFactory typeAdapterFactory;
    private GsonBuilder builder;

    public JsonUtils setTypeAdapterFactory(UtilsTypeAdapterFactory typeAdapterFactory) {
        this.typeAdapterFactory = typeAdapterFactory;
        return this;
    }

    public UtilsTypeAdapterFactory getTypeAdapterFactory() {
        if (this.typeAdapterFactory == null) {
            this.typeAdapterFactory = new UtilsTypeAdapterFactory();
        }
        return typeAdapterFactory;
    }

    public final String toJson(Object obj) throws NullPointerException, InvalidParameterException {
        return toJson(obj, (TypeAdapter<?>)null);
    }

    public  final String toJson(Object obj, TypeAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        if (obj == null) return null;
        return createGson(obj.getClass(), adapter).toJson(obj);
    }

    public  final String toJson(Object obj, Type type) throws NullPointerException, InvalidParameterException {
        if (obj == null) return null;
        return createGson(obj.getClass(), null).toJson(obj, type);
    }

    public final <T> T fromJson(String json, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<T> adapter) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter).fromJson(json, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT) throws NullPointerException, InvalidParameterException, IOException {
        return fromJson(reader, classOfT, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<T> adapter) throws NullPointerException, InvalidParameterException, IOException {
        return createGson(classOfT, adapter).fromJson(reader.getGson(), classOfT);
    }

    protected Gson createGson(Class<?> classOfT, TypeAdapter<?> typeAdapter) {
        if (getGsonBuilder() == null) {
            initGsonBuilder();
        }

        if (typeAdapter == null && getTypeAdapterFactory() != null) {
            typeAdapter = getTypeAdapterFactory().create(classOfT);
        }

        if (typeAdapter != null) {
            getGsonBuilder().registerTypeAdapter(classOfT, typeAdapter);
        } else {
            Log.warn(this, String.format(
                    "Could not find adapter of %s.",
                    classOfT.getSimpleName()));
        }

        return createGson();
    }

    protected Gson createGson() {
        return getGsonBuilder().create();
    }

    protected void initGsonBuilder() {
        this.builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(getTypeAdapterFactory())
                .excludeFieldsWithoutExposeAnnotation();
    }

    protected GsonBuilder getGsonBuilder() {
        return builder;
    }
}
