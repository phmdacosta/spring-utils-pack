package com.pedrocosta.utils.jsonmanager.adapter;

import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.stream.JsonReader;

import java.io.IOException;

public abstract class WriteTypeAdapter<T> extends UtilsTypeAdapter<T> {
    @Override
    public final T read(JsonReader reader) throws NotSupportedException {
        throw new NotSupportedException("Can't read with WriteTypeAdapter.");
    }

    @Override
    public final T read(com.google.gson.stream.JsonReader reader) throws IOException {
        return super.read(reader);
    }

    @Override
    protected final T readJson(com.google.gson.stream.JsonReader reader) throws IOException, NotSupportedException {
        return super.readJson(reader);
    }
}
