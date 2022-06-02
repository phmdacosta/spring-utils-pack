package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.AppProperties;
import com.pedrocosta.utils.PackageUtils;
import com.pedrocosta.utils.output.Log;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class UtilsTypeAdapterFactory implements TypeAdapterFactory {
    private static final String ADAPTER_SUFFIX = "Adapter";
    private static final String PROJECT_PACKAGE = "project.package";
    private static final String PROP_ADAPTER_SUFFIX = "json.adapter.suffix";

    private String packageUri;

    public String getPackageUri() {
        return packageUri;
    }

    public UtilsTypeAdapterFactory setPackageUri(String packageUri) {
        this.packageUri = packageUri;
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

        // Verify properties file
        String adapterName = getAdapterNameFromProperties(classSimpleName);
        if (adapterName != null && !adapterName.isEmpty()) {
            adapter = create(adapterName);
        }

        if (adapter == null) {
            // Look for all subpackages into service
            String packageName = getAdapterPackage(clazz);
            List<Package> subPackages = PackageUtils.getSubPackages(packageName);

            while (adapter == null && !"".equals(packageName)) {
                adapter = findAdapterInPackages(subPackages, classSimpleName, businessType);
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
            adapter = findAdapterByName(className);
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }

    /**
     * Get suffix of adapter's name.
     * <br>
     * Adapter's suffix can be set by property '<i><code>json.adapter.suffix</code></i>'
     * in application properties file.
     */
    protected String getAdapterSuffix() {
        String propAdapterSuffix = AppProperties.get(PROP_ADAPTER_SUFFIX);
        if (propAdapterSuffix != null) {
            return propAdapterSuffix;
        }
        return ADAPTER_SUFFIX;
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

    /**
     * Build adapter class name with its package.
     *
     * Pattern of adapter's name is: Object class name + type + Adapter
     * Ex: MyObjectReadAdapter
     *
     * @param packageName       Name of package of adapter classes
     * @param classSimpleName   Class simple name of object to be deserialized
     * @param type              If we use different types of adapters with different implementation,
     *                          use this parameter to define which type are looking for     *
     * @return Adapter complete name with package.
     */
    protected final String getAdapterName(String packageName, String classSimpleName, String type) {
        String typeCap = "";

        if (type != null && !type.isEmpty()) {
            typeCap = StringUtils.capitalize(type);
        }

        return packageName + "." + typeCap + classSimpleName
                + getAdapterSuffix();
    }

    protected final String getAdapterNameFromProperties(String simpleName) {
        return AdapterProperties.get(simpleName);
    }

    protected <T> TypeAdapter<T> findAdapterInPackages(List<Package> subPackages, String className, String type) {
        TypeAdapter<T> adapter = null;
        for (Package pack : subPackages) {
            adapter = create(getAdapterName(pack.getName(), className, type));
            if (adapter != null) {
                break;
            }
        }
        return adapter;
    }

    protected <T> TypeAdapter<T> findAdapterByName(String name) {
        TypeAdapter<T> adapter = null;
        try {
            Class<TypeAdapter<T>> adapterClass = (Class<TypeAdapter<T>>) Class.forName(name);
            if (adapterClass.getConstructors().length > 0) {
                adapter = adapterClass.getConstructor(new Class[0]).newInstance();
            }
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return adapter;
    }

    protected NullPointerException getPackageEmptyException() {
        return new NullPointerException("Could not get package uri. " +
                "Set project.package property or use setPackageUri method.");
    }
}
