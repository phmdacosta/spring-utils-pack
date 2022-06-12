package com.pedrocosta.springutils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {

    private static final String FILE_SEPARATOR = File.separator;

    /**
     * Find class by its name.
     * @param name Name of class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> findClassByName(String name) throws ClassNotFoundException {
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

    private static List<String> getClassNamesFromJar(String jarEncode, String packageName) throws IOException {
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

    private static List<String> getClassNamesFromFilePath(String path, String packageName) throws URISyntaxException {
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

    private static List<String> getFileNamesFromFolder(File folder, boolean withPath) {
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
