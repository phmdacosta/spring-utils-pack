package com.pedrocosta.springutils.jsonmanager.adapter;

import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public interface JsonWriteAdapter<T> extends JsonAdapter<T> {
    void write(JsonWriter writer, T obj) throws IOException, NotSupportedException;
}
