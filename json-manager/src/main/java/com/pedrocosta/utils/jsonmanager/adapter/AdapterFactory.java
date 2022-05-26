package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.pedrocosta.utils.AppProperties;
import com.pedrocosta.utils.PackageUtils;
import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class AdapterFactory implements TypeAdapterFactory {
    private static final String ADAPTER_SUFFIX = "Adapter";
    private static final String ADAPTER_PACKAGE = "adapters";
    private static final String PROJECT_PACKAGE = "project.package";

    private String packageUri;

    public AdapterFactory setPackageUri(String packageUri) {
        this.packageUri = packageUri;
        return this;
    }

    public String getPackageUri() {
        return packageUri;
    }

    /**
     * Create a new instance of adapter.
     *
     * @param clazz Class of object to be deserialized
     * @param <T>   Type class of object to be deserialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz) {
        return create(clazz, null);
    }

    /**
     * Create a new instance of adapter.
     *
     * @param clazz Class of object to be deserialized
     * @param type  If we use different types of adapters with different implementation,
     *      *              use this parameter to define which type are looking for
     * @param <T>   Type class of object to be deserialized
     *
     * @return {@link TypeAdapter} instance.
     */
    public <T> TypeAdapter<T> create(Class<T> clazz, String type) {
        TypeAdapter<T> adapter = null;

        // Look for all subpackages into service
        String packageName = getAdapterPackage();
        List<Package> subPackages = PackageUtils.getSubPackages(packageName);

        while (adapter == null && !"".equals(packageName)) {
            adapter = findAdapterInPackages(subPackages, clazz, type);
            packageName = PackageUtils.getParentPackageName(packageName);
            subPackages = PackageUtils.getSubPackages(packageName);
        }

        return adapter;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        try {
            return findAdapter(typeToken.getType().getTypeName()
                    .concat(getAdapterSuffix()));
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }
        return null;
    }

    /**
     * Get suffix of adapter's name.
     */
    protected String getAdapterSuffix() {
        return ADAPTER_SUFFIX;
    }

    /**
     * Get package name of adapters.
     */
    protected String getAdapterPackage() {
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
     * @param packageName   Name of package of adapter classes
     * @param clazz         Class of object to be deserialized
     * @param type          If we use different types of adapters with different implementation,
     *                      use this parameter to define which type are looking for
     * @param <T>           Type class of object to be deserialized
     *
     * @return Adapter complete name with package.
     */
    protected final <T> String getAdapterName(String packageName, Class<T> clazz, String type) {
        String typeCap = "";

        if (type != null && !type.isEmpty()) {
            typeCap = StringUtils.capitalize(type);
        }

        return packageName + "." + typeCap + clazz.getSimpleName()
                + getAdapterSuffix();
    }

    protected <T> TypeAdapter<T> findAdapterInPackages(List<Package> subPackages, Class<T> clazz, String type) {
        TypeAdapter<T> adapter = null;

        for (Package pack : subPackages) {
            try {
                adapter = findAdapter(getAdapterName(pack.getName(), clazz, type));
                if (adapter != null) {
                    break;
                }
            } catch (Exception e) {
                Log.warn(this, e.getMessage());
            }
        }

        return adapter;
    }

    protected <T> TypeAdapter<T> findAdapter(String name) throws ClassNotFoundException {
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
