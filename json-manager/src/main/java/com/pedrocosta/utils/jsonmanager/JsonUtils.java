package com.pedrocosta.utils.jsonmanager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.pedrocosta.utils.jsonmanager.adapter.AdapterFactory;
import com.pedrocosta.utils.output.Log;
import com.pedrocosta.utils.output.Messages;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

@Component
public class JsonUtils {

    private final AdapterFactory adapterFactory;
    private GsonBuilder builder;
    private boolean useExposedAnnotation;

    public JsonUtils(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    public final String toJson(Object obj) throws NullPointerException, InvalidParameterException {
        return toJson(obj, null,null);
    }

    public  final String toJson(Object obj, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return toJson(obj, adapter, null);
    }

    public final String toJson(Object obj, String type) throws NullPointerException, InvalidParameterException {
        return toJson(obj, null, type);
    }

    public final String toJson(Object obj, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(obj.getClass(), adapter, type).toJson(obj);
    }

    public final <T> T fromJson(String json, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, adapter, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null, type);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(json, classOfT);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, null, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter adapter) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, adapter, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
        return fromJson(reader, classOfT, null, type);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        return createGson(classOfT, adapter, type).fromJson(reader, classOfT);
    }

    public JsonUtils setUseExposedAnnotation(boolean useExposedAnnotation) {
        this.useExposedAnnotation = useExposedAnnotation;
        return this;
    }

    private <T> Gson createGson(Class<T> classOfT, TypeAdapter adapter, String type) throws NullPointerException, InvalidParameterException {
        if (builder == null) {
            builder = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }

        if (useExposedAnnotation) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }

        Gson gson = builder.create();

        if (adapter == null && adapterFactory != null) {
            adapter = adapterFactory.create(classOfT, type);
        }

        if (adapter != null) {
            gson = builder.registerTypeAdapter(classOfT, adapter).create();
        } else {
            Log.warn(this, String.format(
                    "Could not find adapter of %s for api type %s.",
                    classOfT.getSimpleName(), type));
        }

        return gson;
    }
}
