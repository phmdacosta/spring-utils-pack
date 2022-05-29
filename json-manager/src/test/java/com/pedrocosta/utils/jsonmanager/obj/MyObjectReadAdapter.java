package com.pedrocosta.utils.jsonmanager.obj;

import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.adapter.JsonReadAdapter;
import com.pedrocosta.utils.jsonmanager.stream.JsonReader;

import java.io.IOException;

public class MyObjectReadAdapter implements JsonReadAdapter<MyObject> {
    protected final String ID = "id";
    protected final String STRING = "string";
    protected final String DOUBLE = "double";
    protected final String DATE = "date";

    @Override
    public MyObject read(JsonReader reader) throws IOException, NotSupportedException {
        MyObject myObject = new MyObject();
        myObject.setId(reader.get(ID, Long.class));
        myObject.setString(reader.get(STRING, String.class));
        myObject.setDoubl(reader.get(DOUBLE, Double.class));
        myObject.setDate(DateUtils.stringToDate(reader.get(DATE, String.class)));
        return myObject;
    }
}
