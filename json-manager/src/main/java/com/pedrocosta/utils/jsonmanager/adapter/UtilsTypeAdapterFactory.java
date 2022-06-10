package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.AppProperties;
import com.pedrocosta.utils.ClassUtils;
import com.pedrocosta.utils.PackageUtils;
import com.pedrocosta.utils.jsonmanager.adapter.annotation.JsonAdapter;
import com.pedrocosta.utils.output.Log;

import java.io.IOException;
import java.util.*;

public class UtilsTypeAdapterFactory implements TypeAdapterFactory {
    private static final String PROJECT_PACKAGE = "project.package";

    private String packageUri;
    private boolean useAnnotation;
    private Map<Class<?>, String> availableAdapterNames;

    public String getPackageUri() {
        return packageUri;
    }

    public UtilsTypeAdapterFactory setPackageUri(String packageUri) {
        this.packageUri = packageUri;
        return this;
    }

    public UtilsTypeAdapterFactory useAnnotation(boolean useAnnotation) {
        this.useAnnotation = useAnnotation;
        return this;
    }

    public <T> UtilsTypeAdapterFactory addAdapter(Class<T> typeClass, Class<? extends TypeAdapter<T>> adapterClass) {
        this.addAdapterName(typeClass, adapterClass.getName());
        return this;
    }

    public <T> UtilsTypeAdapterFactory addAdapterName(Class<T> typeClass, String adapterClassName) {
        if (this.availableAdapterNames == null) {
            this.availableAdapterNames = new HashMap<>();
        }
        this.availableAdapterNames.put(typeClass, adapterClassName);
        return this;
    }

    public <T> UtilsTypeAdapterFactory addAllAdapters(Map<Class<T>, Class<? extends TypeAdapter<T>>> adapterNames) {
        for (Map.Entry<Class<T>, Class<? extends TypeAdapter<T>>> entry : adapterNames.entrySet()) {
            this.addAdapterName(entry.getKey(), entry.getValue().getName());
        }
        return this;
    }

    public <T> UtilsTypeAdapterFactory addAllAdapterNames(Map<Class<T>, String> adapterNames) {
        for (Map.Entry<Class<T>, String> entry : adapterNames.entrySet()) {
            this.addAdapterName(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Create method of gson's adapter factory
     * @param gson      Gson object.
     * @param typeToken Gson's type token of object to serialize
     * @param <T>       Type class of object to be deserialized
     * @return {@link TypeAdapter} instance.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        TypeAdapter<T> adapter = null;
        try {
            adapter = create((Class<T>)typeToken.getRawType());
            if (adapter == null) {
                TypeAdapter<T> delegate = gson.getDelegateAdapter(this, typeToken);
                adapter = new TypeAdapter<T>() {
                    @Override
                    public void write(JsonWriter writer, T t) throws IOException {
                        if (t == null) {
                            writer.nullValue();
                        } else {
                            delegate.write(writer, t);
                        }
                    }
                    @Override
                    public T read(JsonReader reader) throws IOException {
                        return delegate.read(reader);
                    }
                };
            }
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }

    /**
     * Create a new instance of adapter.
     *
     * @param clazz Class of object to be deserialized
     * @param <T>   Type class of object to be serialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz) {
        return create(clazz, null);
    }

    /**
     * Create a new instance of adapter.
     * <br>
     * Pattern of adapter's name is: Object class name + business type + Adapter
     * Ex: MyObjectReadAdapter
     *
     * @param clazz         Class of object to be serialized
     * @param businessType  If we use different types of adapters with different implementation,
     *                      use this parameter to define which business type are looking for
     * @param <T>           Type class of object to be deserialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz, String businessType) {
        TypeAdapter<T> adapter = null;
        String classSimpleName = clazz.getSimpleName();

        // First look at local config
        if (this.availableAdapterNames != null && !this.availableAdapterNames.isEmpty()) {
            for (Map.Entry<Class<?>, String> entry : availableAdapterNames.entrySet()) {
                if (entry.getKey().equals(clazz)) {
                    adapter = create(entry.getValue());
                    break;
                }
            }
        }

        if (adapter == null) {
            // Verify properties file
            String adapterName = getAdapterNameFromProperties(classSimpleName);
            if (adapterName != null && !adapterName.isEmpty()) {
                adapter = create(adapterName);
            }
        }

        if (adapter == null) {
            // For the last, let's look for all packages into project
            String packageName = getAdapterPackage(clazz);
            List<Package> subPackages = PackageUtils.getSubPackages(packageName);

            if (this.useAnnotation) {
                try {
                    List<TypeAdapter<T>> adapters = AdapterFinder.findAllAnnotated(packageName);
                } catch (Exception e) {
                    Log.warn(this, e.getMessage());
                }
            }

            while (adapter == null && !"".equals(packageName)) {
                try {
                    adapter = AdapterFinder.findAdapterInPackages(subPackages, classSimpleName, businessType);
                    if (this.useAnnotation && !ClassUtils.hasAnnotation(adapter.getClass(), JsonAdapter.class)) {
                        adapter = null;
                    }
                } catch (Exception e) {
                    Log.warn(this, e.getMessage());
                }
                packageName = PackageUtils.getParentPackageName(packageName);
                subPackages = PackageUtils.getSubPackages(packageName);
            }
        }

        return adapter;
    }

    /**
     * Create a new instance of adapter based on completed adapter's class name.
     *
     * @param className Name of the adapter's class
     * @param <T>       Type class of object to be serialized
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(String className) {
        TypeAdapter<T> adapter = null;
        try {
            adapter = AdapterFinder.findAdapterByName(className);
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }

    /**
     * Get package name of adapters.
     */
    protected <T> String getAdapterPackage(Class<T> clazz) {
        if (clazz != null && clazz.getPackage() != null) {
            return clazz.getPackage().getName();
        }
        String packUri = getPackageUri();
        if (packUri == null || packUri.isEmpty()) {
            try {
                packUri = AppProperties.get(PROJECT_PACKAGE);
                if (packUri == null || packUri.isEmpty()) {
                    throw getPackageEmptyException();
                }
            } catch (NullPointerException e) {
                throw getPackageEmptyException();
            }
        }
        return packUri;
    }

    protected final String getAdapterNameFromProperties(String simpleName) {
        return AdapterProperties.get(simpleName);
    }

    protected NullPointerException getPackageEmptyException() {
        return new NullPointerException("Could not get package uri. " +
                "Set project.package property or use setPackageUri method.");
    }
}
