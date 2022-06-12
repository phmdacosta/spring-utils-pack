package com.pedrocosta.springutils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class PropertiesUtils {
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
            if (propFileName != null && propFileName.endsWith(".properties")) {
                prefixPropFileName = propFileName.replace(".properties","");
            }
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resourceFiles = resolver.getResources("classpath*:"+prefixPropFileName+"*.properties");
            Resource[] testResources = getTestResources(resourceFiles);
            if (!ArrayUtils.isEmpty(testResources)) {
                loadPropertiesFiles(testResources);
            } else {
                loadPropertiesFiles(resourceFiles);
            }
        } catch (IOException | NullPointerException ignored) {}
    }

    private void loadPropertiesFiles(Resource[] resourceFiles) throws IOException {
        for (Resource resource : resourceFiles) {
            loadPropertiesFile(resource);
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
            if (resourceFile.getURL().getPath().contains("test")) {
                testResources.add(resourceFile);
            }
        }
        return testResources.toArray(new Resource[0]);
    }
}
