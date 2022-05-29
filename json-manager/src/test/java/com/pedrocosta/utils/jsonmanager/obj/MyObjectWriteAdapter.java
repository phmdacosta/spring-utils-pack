package com.pedrocosta.utils.jsonmanager.obj;

import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.adapter.JsonWriteAdapter;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public class MyObjectWriteAdapter implements JsonWriteAdapter<MyObject> {
    protected final String ID = "id";
    protected final String STRING = "string";
    protected final String DOUBLE = "double";
    protected final String DATE = "date";

    @Override
    public void write(JsonWriter writer, MyObject obj) throws IOException, NotSupportedException {
        writer.add(ID, obj.getId());
        writer.add(STRING, obj.getString());
        writer.add(DOUBLE, obj.getDoubl());
        writer.add(DATE, DateUtils.dateToString(obj.getDate()));
    }
}
