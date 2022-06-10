package com.pedrocosta.utils.jsonmanager.adapter;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.AppProperties;
import com.pedrocosta.utils.ClassFinder;
import com.pedrocosta.utils.ClassUtils;
import com.pedrocosta.utils.PackageUtils;
import com.pedrocosta.utils.jsonmanager.adapter.annotation.JsonAdapter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class AdapterFinder extends ClassFinder {

    private static final String ADAPTER_SUFFIX = "Adapter";
    private static final String PROP_ADAPTER_SUFFIX = "json.adapter.suffix";

    /**
     * Find {@link TypeAdapter} object by its class name.
     * <br>
     * It builds the class name by the following pattern:
     * Object class name + type + Adapter
     *
     * @param packages  List of packages to look
     * @param className Class name to the object of the adapter
     *
     * @return {@link TypeAdapter} object
     */
    public static <T> TypeAdapter<T> findAdapterInPackages(List<Package> packages, String className) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, URISyntaxException {
        return findAdapterInPackages(packages, className, false);
    }

    /**
     * Find {@link TypeAdapter} object by its class name.
     * <br>
     * It builds the class name by the following pattern:
     * Object class name + type + Adapter
     *
     * @param packages              List of packages to look
     * @param className             Class name to the object of the adapter
     * @param searchForAnnotation   Search or not for adapters with {@link JsonAdapter} annotation.
     *
     * @return {@link TypeAdapter} object
     */
    public static <T> TypeAdapter<T> findAdapterInPackages(List<Package> packages, String className, boolean searchForAnnotation) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, URISyntaxException {
        return findAdapterInPackages(packages, className, null, searchForAnnotation);
    }

    /**
     * Find {@link TypeAdapter} object by its class name.
     * <br>
     * It builds the class name by the following pattern:
     * Object class name + type + Adapter
     *
     * @param packages  List of packages to look
     * @param className Class name to the object of the adapter
     * @param type      Business type (if exists)
     *
     * @return {@link TypeAdapter} object
     */
    public static <T> TypeAdapter<T> findAdapterInPackages(List<Package> packages, String className, String type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, URISyntaxException {
        return findAdapterInPackages(packages, className, type, false);
    }

    /**
     * Find {@link TypeAdapter} object by its class name.
     * <br>
     * It builds the class name by the following pattern:
     * Object class name + type + Adapter
     *
     * @param packages              List of packages to look
     * @param className             Class name to the object of the adapter
     * @param type                  Business type (if exists)
     * @param searchForAnnotation   Search or not for adapters with {@link JsonAdapter} annotation.
     *
     * @return {@link TypeAdapter} object
     */
    public static <T> TypeAdapter<T> findAdapterInPackages(List<Package> packages, String className, String type, boolean searchForAnnotation) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, URISyntaxException {
        TypeAdapter<T> adapter = null;
        for (Package pack : packages) {
            try {
                if (searchForAnnotation) {
                    List<TypeAdapter<T>> annotAdapters = findAllAnnotated(pack.getName());
                    for (TypeAdapter<T> typeAdapter : annotAdapters) {
                        if (typeAdapter.getClass().getSimpleName()
                                .startsWith(getAdapterPrefix(className, type))){
                            adapter = typeAdapter;
                        }
                    }
                } else {
                    adapter = findAdapterByName(getAdapterName(pack.getName(), className, type));
                }
                if (adapter != null) {
                    break;
                }
            } catch (ClassNotFoundException ignored) {}
        }
        return adapter;
    }

    /**
     * Find {@link TypeAdapter} object by its class name.
     *
     * @param name Name of class
     * @return {@link TypeAdapter} object
     */
    @SuppressWarnings("unchecked")
    public static <T> TypeAdapter<T> findAdapterByName(String name) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        TypeAdapter<T> adapter = null;
        Class<TypeAdapter<T>> adapterClass = (Class<TypeAdapter<T>>) Class.forName(name);
        if (adapterClass.getConstructors().length > 0) {
            adapter = adapterClass.getConstructor(new Class[0]).newInstance();
        }
        return adapter;
    }

    /**
     * Find all {@link TypeAdapter} objects that have
     * {@link JsonAdapter} annotation in a list of packages.
     *
     * @param packageName Package name to look.
     * @return  List of {@link TypeAdapter} objects.
     */
    public static <T> List<TypeAdapter<T>> findAllAnnotated(String packageName) throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<TypeAdapter<T>> adapters = new ArrayList<>();
        List<Package> subPackages = PackageUtils.getSubPackages(packageName);
        for (Package pack : subPackages) {
            List<String> classNames = findClassNamesFromPackage(pack.getName());
            for (String actual : classNames) {
                if (actual != null) {
                    Class<?> adapterClass = null;
                    try {
                        adapterClass = findClassByName(actual);
                    } catch (ClassNotFoundException ignored) {}

                    if (adapterClass != null && ClassUtils.hasAnnotation(adapterClass, JsonAdapter.class)) {
                        adapters.add(findAdapterByName(adapterClass.getName()));
                    }
                }
            }
        }
        return adapters;
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
    protected static String getAdapterName(String packageName, String classSimpleName, String type) {
        return packageName + "." + getAdapterPrefix(classSimpleName, type)
                + getAdapterSuffix();
    }

    protected static String getAdapterPrefix(String classSimpleName, String type) {
        String typeCap = "";
        if (type != null && !type.isEmpty()) {
            typeCap = StringUtils.capitalize(type);
        }
        return typeCap + classSimpleName;
    }

    /**
     * Get suffix of adapter's name.
     * <br>
     * Adapter's suffix can be set by property '<i><code>json.adapter.suffix</code></i>'
     * in application properties file.
     */
    protected static String getAdapterSuffix() {
        String propAdapterSuffix = AppProperties.get(PROP_ADAPTER_SUFFIX);
        if (propAdapterSuffix != null) {
            return propAdapterSuffix;
        }
        return ADAPTER_SUFFIX;
    }
}
