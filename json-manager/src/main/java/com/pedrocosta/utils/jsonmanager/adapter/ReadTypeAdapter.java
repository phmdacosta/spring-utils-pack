package com.pedrocosta.utils.jsonmanager.adapter;

import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public abstract class ReadTypeAdapter<T> extends UtilsTypeAdapter<T> {
    @Override
    public final void write(JsonWriter writer, T obj) throws NotSupportedException {
        throw new NotSupportedException("Can't write with ReadTypeAdapter.");
    }

    @Override
    public final void write(com.google.gson.stream.JsonWriter writer, T t) throws IOException {
        super.write(writer, t);
    }

    @Override
    protected final void writeJson(com.google.gson.stream.JsonWriter writer, T obj) throws IOException, NotSupportedException {
        super.writeJson(writer, obj);
    }
}
