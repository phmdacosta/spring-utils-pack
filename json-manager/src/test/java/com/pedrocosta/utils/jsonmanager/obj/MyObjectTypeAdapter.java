package com.pedrocosta.utils.jsonmanager.obj;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.jsonmanager.adapter.JsonTypeAdapter;
import com.pedrocosta.utils.exception.NotSupportedException;

import java.io.IOException;

public class MyObjectTypeAdapter extends JsonTypeAdapter<MyObject> {
    protected final String ID = "id";
    protected final String STRING = "string";
    protected final String DOUBLE = "double";
    protected final String DATE = "date";

    @Override
    protected void writeJson(JsonWriter writer, MyObject obj) throws IOException, NotSupportedException {
        com.pedrocosta.utils.jsonmanager.stream.JsonWriter jsonWriter
                = new com.pedrocosta.utils.jsonmanager.stream.JsonWriter(writer);
        jsonWriter.add(ID, obj.getId());
        jsonWriter.add(STRING, obj.getString());
        jsonWriter.add(DOUBLE, obj.getDoubl());
        jsonWriter.add(DATE, DateUtils.dateToString(obj.getDate()));
    }

    @Override
    protected MyObject readJson(JsonReader reader) throws IOException, NotSupportedException {
        com.pedrocosta.utils.jsonmanager.stream.JsonReader jsonReader
                = new com.pedrocosta.utils.jsonmanager.stream.JsonReader(reader);

        MyObject myObject = new MyObject();
        myObject.setId(jsonReader.get(ID, Long.class));
        myObject.setString(jsonReader.get(STRING, String.class));
        myObject.setDoubl(jsonReader.get(DOUBLE, Double.class));
        myObject.setDate(DateUtils.stringToDate(jsonReader.get(DATE, String.class)));
        return myObject;
    }
}
