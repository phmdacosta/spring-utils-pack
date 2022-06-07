package com.pedrocosta.utils.jsonmanager.adapter;

import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.stream.JsonReader;

import java.io.IOException;

public interface JsonReadAdapter<T> extends JsonAdapter<T> {
    T read(JsonReader reader) throws IOException, NotSupportedException;
}
