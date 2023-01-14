package com.pedrocosta.springutils;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Util class to handle packages.
 *
 * @author Pedro H. M. da Costa
 * @version 1.0
 */
public class PackageUtils {

    public static Package getProjectPackage(ApplicationContext context) throws ClassNotFoundException {
        Package pack = null;

        Map<String, Object> candidates = context.getBeansWithAnnotation(SpringBootApplication.class);
        if (!candidates.isEmpty()) {
            pack = candidates.values().toArray()[0].getClass().getPackage();
        } else {
            String packageName = context.getEnvironment().getProperty("project.package");
            if (packageName != null) {
                pack = getPackage(packageName);
            }
        }

        if (pack == null) {
            throw new ClassNotFoundException("Could not find any application class annotated with SpringBootApplication or 'project.package' in property files.");
        }

        return pack;
    }

    /**
     * Get sub packages from a super one.
     *
     * @param packageName Name of super package
     *
     * @return List of {@link Package} objects.
     */
    public static List<Package> getSubPackages(final String packageName) {
        return Arrays.stream(Package.getPackages())
                .filter(pack ->
                        pack.getName().startsWith(packageName) //filter by name
                ).collect(Collectors.toList());
    }

    public static Package getPackage(String packageName) {
        return Arrays.stream(Package.getPackages())
                .filter(pack -> pack.getName().equals(packageName))
                .findFirst().orElse(null);
    }

    public static String getParentPackageName(String packageName) {
        String[] packageParts = packageName.split("\\.");
        packageParts[packageParts.length - 1] = "";
        final StringBuilder result = new StringBuilder();
        Arrays.stream(packageParts).forEach(pack -> result.append(".").append(pack));
        return result.toString().replaceFirst("\\.", "");
    }
}
