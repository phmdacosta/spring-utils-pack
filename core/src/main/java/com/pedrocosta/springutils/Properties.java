package com.pedrocosta.springutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.stream.Collectors;

public class Properties extends java.util.Properties {

    private List<String> exclusionList;

    @Override
    public synchronized void loadFromXML(InputStream in) throws IOException {
        super.loadFromXML(in);
        this.exclude();
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        super.load(reader);
        this.exclude();
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        super.load(inStream);
        this.exclude();
    }

    public void excludePasswords(boolean exclude) {
        if (exclude) {
            this.excludeKeysWith("password");
        }
    }

    public void excludeKeysWith(String ... keys) {
        this.addExclusions(keys);
    }

    private void addExclusions(String ... keys) {
        if (this.exclusionList == null || this.exclusionList.isEmpty())
            this.exclusionList = new ArrayList<>();
        this.exclusionList.addAll(Arrays.asList(keys));
    }

    private void exclude() {
        this.keySet().stream().filter(k ->
                this.exclusionList.stream().anyMatch(x -> ((String)k).contains(x))
        ).collect(Collectors.toList()).forEach(this::remove);
    }
}
