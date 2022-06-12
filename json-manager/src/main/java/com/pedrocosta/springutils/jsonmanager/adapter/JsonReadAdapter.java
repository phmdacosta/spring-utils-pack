package com.pedrocosta.springutils.jsonmanager.adapter;

import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.stream.JsonReader;

import java.io.IOException;

public interface JsonReadAdapter<T> extends JsonAdapter<T> {
    T read(JsonReader reader) throws IOException, NotSupportedException;
}
