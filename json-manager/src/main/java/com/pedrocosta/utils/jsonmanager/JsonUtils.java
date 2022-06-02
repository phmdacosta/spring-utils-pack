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
//    private boolean useExposedAnnotation;
//    private String adapterSuffix;

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

//    public JsonUtils setUseExposedAnnotation(boolean useExposedAnnotation) {
//        this.useExposedAnnotation = useExposedAnnotation;
//        return this;
//    }
//
//    public JsonUtils setAdapterSuffix(String adapterSuffix) {
//        this.adapterSuffix = adapterSuffix;
//        return this;
//    }

    public final String toJson(Object obj) throws NullPointerException, InvalidParameterException {
        return toJson(obj, (TypeAdapter<?>)null);
    }

    public  final String toJson(Object obj, TypeAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
        if (obj == null) return null;
        return createGson(obj.getClass(), adapter).toJson(obj);
    }

//    public  final String toJson(Object obj, JsonWriteAdapter<?> adapter) throws NullPointerException, InvalidParameterException {
////        return toJson(obj, adapter, null);
//        return createGson(obj.getClass(), adapter, null);
//    }

//    public final String toJson(Object obj, String type) throws NullPointerException, InvalidParameterException {
//        return toJson(obj, (JsonWriteAdapter<?>)null, type);
//    }

    public  final String toJson(Object obj, Type type) throws NullPointerException, InvalidParameterException {
        if (obj == null) return null;
        return createGson(obj.getClass(), null).toJson(obj, type);
    }

//    public final String toJson(Object obj, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
//        return createGson(obj.getClass(), adapter, type).toJson(obj);
//    }
//
//    public final String toJson(Object obj, JsonWriteAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
//        if (obj == null) return "";
//        return createGson(obj.getClass(), null, adapter, type).toJson(obj);
//    }

    public final <T> T fromJson(String json, Class<T> classOfT) throws NullPointerException, InvalidParameterException {
        return fromJson(json, classOfT, null);
    }

    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<T> adapter) throws NullPointerException, InvalidParameterException {
//        return fromJson(json, classOfT, adapter, null);
        return createGson(classOfT, adapter).fromJson(json, classOfT);
    }

//    public final <T> T fromJson(String json, Class<T> classOfT, JsonReadAdapter<T> adapter) throws NullPointerException, InvalidParameterException {
//        return fromJson(json, classOfT, adapter, null);
//    }

//    public final <T> T fromJson(String json, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException {
//        return fromJson(json, classOfT, (JsonReadAdapter<T>)null, type);
//    }
//
//    public final <T> T fromJson(String json, Class<T> classOfT, TypeAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException {
//        return createGson(classOfT, adapter, type).fromJson(json, classOfT);
//    }

//    public final <T> T fromJson(String json, Class<T> classOfT, JsonReadAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException {
//        return createGson(classOfT, null, adapter, type).fromJson(json, classOfT);
//    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT) throws NullPointerException, InvalidParameterException, IOException {
        return fromJson(reader, classOfT, null);
    }

    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<T> adapter) throws NullPointerException, InvalidParameterException, IOException {
        return createGson(classOfT, adapter).fromJson(reader.getGson(), classOfT);
    }

//    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, String type) throws NullPointerException, InvalidParameterException, IOException {
//        return fromJson(reader, classOfT, (JsonReadAdapter<T>)null, type);
//    }

//    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, TypeAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException, IOException {
//        return createGson(classOfT, adapter, type).fromJson(reader.getGson(), classOfT);
//    }

//    public final <T> T fromJson(JsonReader reader, Class<T> classOfT, JsonReadAdapter<T> adapter, String type) throws NullPointerException, InvalidParameterException, IOException {
//        return createGson(classOfT, null, adapter, type).fromJson(reader.getGson(), classOfT);
//    }

//    protected Gson createGson(Class<?> classOfT, TypeAdapter<?> adapter, String type) throws NullPointerException, InvalidParameterException {
//        return createGson(classOfT, adapter, null, type);
//    }

//    protected Gson createGson(Class<?> classOfT, TypeAdapter<?> typeAdapter, JsonAdapter<?> jsonAdapter, String type) throws NullPointerException, InvalidParameterException {
    protected Gson createGson(Class<?> classOfT, TypeAdapter<?> typeAdapter) {
        if (getGsonBuilder() == null) {
            initGsonBuilder();
        }

//        if (useExposedAnnotation) {
//            builder.excludeFieldsWithoutExposeAnnotation();
//        }

//        builder.create();

        //TENTAR USAR O TYPE ADAPTER FACTORY DO GSON, DEPOIS PEGAR O ADAPTER E SETAR O JSONUTILS

//        if (jsonAdapter != null) {
//            JsonTypeAdapter<?> jsonTypeAdapter = new JsonTypeAdapter<>(typeAdapterFactory);
//            try {
//                jsonTypeAdapter.setReadAdapter((JsonReadAdapter) jsonAdapter);
//            } catch (ClassCastException ignored) {
//            }
//            try {
//                jsonTypeAdapter.setWriteAdapter((JsonWriteAdapter) jsonAdapter);
//            } catch (ClassCastException ignored) {
//            }
//            typeAdapter = jsonTypeAdapter;
//        }

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
