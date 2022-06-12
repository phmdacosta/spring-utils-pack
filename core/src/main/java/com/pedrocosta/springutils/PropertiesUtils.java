package com.pedrocosta.springutils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class PropertiesUtils {
    private static final String PROPERTIES_SUFFIX = ".properties";
    private Properties properties;

    public String getProperty(String key) {
        return this.getProperties().getProperty(key);
    }

    protected Properties getProperties() {
        return properties;
    }

    protected void initProperties(String propFileName) {
        this.properties = new Properties();
        try {
            String prefixPropFileName = "application";
            if (propFileName != null && propFileName.endsWith(PROPERTIES_SUFFIX)) {
                prefixPropFileName = propFileName.replace(PROPERTIES_SUFFIX,"");
            }
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resourceFiles = resolver.getResources("classpath*:"+prefixPropFileName+"*"+PROPERTIES_SUFFIX);
            resourceFiles = sort(resourceFiles);
            Resource[] testResources = getTestResources(resourceFiles);
            loadPropertiesFiles(resourceFiles);
            if (!ArrayUtils.isEmpty(testResources)) {
                loadPropertiesFiles(testResources);
            }
        } catch (IOException | NullPointerException ignored) {}
    }

    private Resource[] sort(Resource[] resources) {
        Set<Resource> resourceSet = new TreeSet<>(new ResourceNameComparator());
        resourceSet.addAll(Arrays.asList(resources));
        return resourceSet.toArray(new Resource[0]);
    }

    private void loadPropertiesFiles(Resource[] resourceFiles) throws IOException {
        for (Resource resource : resourceFiles) {
            if (!isTestResource(resource)) loadPropertiesFile(resource);
        }
    }

    private void loadPropertiesFile(Resource resourceFile) throws IOException {
        InputStream resourceStream = resourceFile.getInputStream();
        properties.load(resourceStream);
        resourceStream.close();
    }

    private Resource[] getTestResources(Resource[] resources) throws IOException {
        List<Resource> testResources = new ArrayList<>();
        for (Resource resourceFile : resources) {
            if (isTestResource(resourceFile)) {
                testResources.add(resourceFile);
            }
        }
        return testResources.toArray(new Resource[0]);
    }

    private boolean isTestResource(Resource resource) throws IOException {
        return resource != null && resource.getURL().getPath().contains("test");
    }

    private static class ResourceNameComparator implements Comparator<Resource> {
        @Override
        public int compare(Resource r1, Resource r2) {
            if (r1 == null || r1.getFilename() == null) {
                return 1;
            }
            if (r2 == null || r2.getFilename() == null) {
                return -1;
            }
            String r1Name = r1.getFilename().replace(PROPERTIES_SUFFIX, "");
            String r2Name = r2.getFilename().replace(PROPERTIES_SUFFIX, "");
            return r1Name.compareTo(r2Name);
        }
    }
}
