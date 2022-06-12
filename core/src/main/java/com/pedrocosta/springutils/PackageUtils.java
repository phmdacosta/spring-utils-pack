package com.pedrocosta.springutils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PackageUtils {

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
