package com.pedrocosta.springutils;

import org.reflections.Reflections;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to find classes by reflection.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ClassFinder {

    private static final String FILE_SEPARATOR = File.separator;

    /**
     * Find class by its name.
     * @param name Name of class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> findClassByName(final String name) throws ClassNotFoundException {
        return (Class<T>) Class.forName(name);
    }

    /**
     * Find all class names in a package.
     * @param packageName Package name
     * @return List of names, null if not find anyone.
     */
    public static List<String> findClassNamesFromPackage(String packageName) throws IOException, URISyntaxException {
        List<String> result = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        packageName = packageName.replace(".", FILE_SEPARATOR);
        URL packageURL = classLoader.getResource(packageName);

        if (packageURL == null) {
            return result;
        }

        if(packageURL.getProtocol().equals("jar"))
            result.addAll(getClassNamesFromJar(packageURL.getFile(), packageName));
        else
            result.addAll(getClassNamesFromFilePath(packageURL.toString(), packageName));

        return result;
    }

    public static List<Class<?>> findAllByAssignable(final ApplicationContext context, final Class<?> assignable) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(assignable));
        return findAllByProvider(context, provider);
    }

    public static List<Class<?>> findAllByAnnotation(final Class<? extends Annotation> annotation) throws ClassNotFoundException {
        return findAllByAnnotation(annotation, null);
    }

    public static List<Class<?>> findAllByAnnotation(final Class<? extends Annotation> annotation, final Package projectPackage) throws ClassNotFoundException {
        Package currentPackage = projectPackage;
        if (projectPackage == null) {
            currentPackage = ClassFinder.class.getPackage();
        }
        Reflections reflections = new Reflections(currentPackage.getName());
        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(annotation);
        return new ArrayList<>(allClasses);
    }

    public static List<Class<?>> findAllByAnnotation(final ApplicationContext context, final Class<? extends Annotation> annotation) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotation));
        return findAllByProvider(context, provider);
    }

    private static List<Class<?>> findAllByProvider(final ApplicationContext context, final ClassPathScanningCandidateComponentProvider provider) throws ClassNotFoundException {
        String projectPackageName = PackageUtils.getProjectPackage(context).getName();
        Set<BeanDefinition> candidates = provider.findCandidateComponents(projectPackageName);

        return candidates.stream().flatMap(bean -> {
            try {
                return Stream.of(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                return Stream.empty();
            }
        }).collect(Collectors.toList());
    }

    private static List<String> getClassNamesFromJar(final String jarEncode, final String packageName) throws IOException {
        List<String> names = new ArrayList<>();
        String jarFileName = URLDecoder.decode(jarEncode, "UTF-8");
        jarFileName = jarFileName.substring(5,jarFileName.indexOf("!"));
        JarFile jf = new JarFile(jarFileName);
        Enumeration<JarEntry> jarEntries = jf.entries();
        while(jarEntries.hasMoreElements()){
            String entryName = jarEntries.nextElement().getName();
            if(entryName.startsWith(packageName) && entryName.length()>packageName.length()+5){
                entryName = entryName.substring(packageName.length(),entryName.lastIndexOf('.'));
                names.add(entryName);
            }
        }
        return names;
    }

    private static List<String> getClassNamesFromFilePath(final String path, final String packageName) throws URISyntaxException {
        List<String> result = new ArrayList<>();
        URI uri = new URI(path);
        File folder = new File(uri);
        File[] classFiles = folder.listFiles();
        if (classFiles == null) {
            return result;
        }
        List<String> fileNames = getFileNamesFromFolder(folder, true);
        for (String name : fileNames) {
            if (packageName != null && !packageName.isEmpty()) {
                String folderPath = folder.getPath();
                result.add(name.substring(folderPath.indexOf(packageName))
                        .replace(FILE_SEPARATOR, "."));
            } else {
                result.add(name.substring(name.lastIndexOf(FILE_SEPARATOR) + 1));
            }
        }
        return result;
    }

    private static List<String> getFileNamesFromFolder(final File folder, boolean withPath) {
        List<String> names = new ArrayList<>();
        File[] classFiles = folder.listFiles();
        if (classFiles == null) {
            return names;
        }
        for(File actual: classFiles){
            if (!actual.isDirectory()) {
                String entryName = actual.getName();
                if (withPath) {
                    entryName = actual.getPath();
                }
                entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                names.add(entryName);
            } else {
                names.addAll(getFileNamesFromFolder(actual, withPath));
            }
        }
        return names;
    }
}
