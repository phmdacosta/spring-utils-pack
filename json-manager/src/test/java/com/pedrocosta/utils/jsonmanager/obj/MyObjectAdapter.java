package com.pedrocosta.utils.jsonmanager.obj;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.jsonmanager.adapter.JsonAdapter;
import com.pedrocosta.utils.exception.NotSupportedException;

import java.io.IOException;

public class MyObjectAdapter extends JsonAdapter<MyObject> {
    protected final String ID = "id";
    protected final String STRING = "string";
    protected final String DOUBLE = "double";
    protected final String DATE = "date";
    @Override
    protected void writeJson(JsonWriter writer, MyObject obj) throws IOException, NotSupportedException {
        writer.name(ID);
        writer.value(obj.getId());
        writer.name(STRING);
        writer.value(obj.getString());
        writer.name(DOUBLE);
        writer.value(obj.getDoubl());
        writer.name(DATE);
        writer.value(DateUtils.dateToString(obj.getDate()));
    }

    @Override
    protected MyObject readJson(JsonReader reader) throws IOException, NotSupportedException {
        MyObject myObject = new MyObject();
        String fieldName = "";
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (JsonToken.NAME.equals(token)) {
                fieldName = reader.nextName();
            }

            switch (fieldName) {
                case ID:
                    token = reader.peek();
                    myObject.setId(reader.nextLong());
                    break;
                case STRING:
                    token = reader.peek();
                    myObject.setString(reader.nextString());
                    break;
                case DOUBLE:
                    token = reader.peek();
                    myObject.setDoubl(reader.nextDouble());
                    break;
                case DATE:
                    token = reader.peek();
                    myObject.setDate(
                            DateUtils.stringToDate(reader.nextString()));
                    break;
                default: reader.skipValue();
            }
        }

        return myObject;
    }
}
