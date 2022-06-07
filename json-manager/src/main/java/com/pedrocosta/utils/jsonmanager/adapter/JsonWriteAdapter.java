package com.pedrocosta.utils.jsonmanager.adapter;

import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public interface JsonWriteAdapter<T> extends JsonAdapter<T> {
    void write(JsonWriter writer, T obj) throws IOException, NotSupportedException;
}
