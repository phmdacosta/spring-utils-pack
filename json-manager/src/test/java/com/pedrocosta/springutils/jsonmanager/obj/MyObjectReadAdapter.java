package com.pedrocosta.springutils.jsonmanager.obj;

import com.pedrocosta.springutils.DateUtils;
import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.adapter.ReadTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.stream.JsonReader;

import java.io.IOException;

public class MyObjectReadAdapter extends ReadTypeAdapter<MyObject> implements MyObjectAdapterNames {
    @Override
    public MyObject read(JsonReader reader) throws IOException, NotSupportedException {
        MyObject myObject = new MyObject();
        myObject.setId(reader.get(ID, Long.class));
        myObject.setString(reader.get(STRING, String.class));
        myObject.setDoubl(reader.get(DOUBLE, Double.class));
        myObject.setDate(DateUtils.stringToDate(reader.get(DATE, String.class)));
        myObject.setListStrings(reader.getList(LIST_STRINGS, getJsonUtils(), String.class));
        myObject.setSetStrings(reader.getSet(SET_STRINGS, getJsonUtils(), String.class));
        myObject.setStringArray(reader.getArray(STRING_ARRAY, getJsonUtils(), String.class));
        myObject.setStringArray(reader.getArray(STRING_ARRAY, getJsonUtils(), String.class));
        myObject.setListMyObjects(reader.getList(LIST_OBJECT, getJsonUtils(), MyObject.class));
        myObject.setSetMyObjects(reader.getSet(SET_OBJECT, getJsonUtils(), MyObject.class));
        myObject.setBool(reader.get(BOOLEAN, Boolean.class));
        myObject.setChild(reader.getObject(CHILD, getJsonUtils(), MyObject.class));
        return myObject;
    }
}
