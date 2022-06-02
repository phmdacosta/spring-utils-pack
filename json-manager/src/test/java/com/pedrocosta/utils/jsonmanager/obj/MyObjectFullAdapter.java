package com.pedrocosta.utils.jsonmanager.obj;

import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.utils.jsonmanager.stream.JsonReader;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public class MyObjectFullAdapter extends UtilsTypeAdapter<MyObject> implements MyObjectAdapterNames {

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
        myObject.setListMyObjects(reader.getList(LIST_OBJECT, getJsonUtils(), MyObject.class));
        myObject.setSetMyObjects(reader.getSet(SET_OBJECT, getJsonUtils(), MyObject.class));
        return myObject;
    }

    @Override
    public void write(JsonWriter writer, MyObject obj) throws IOException, NotSupportedException {
        writer.add(ID, obj.getId());
        writer.add(STRING, obj.getString());
        writer.add(DOUBLE, obj.getDoubl());
        writer.add(DATE, DateUtils.dateToString(obj.getDate()));
        writer.addJson(LIST_STRINGS, obj.getListStrings(), getJsonUtils());
        writer.addJson(SET_STRINGS, getJsonUtils().toJson(obj.getSetStrings()));
        writer.addJson(STRING_ARRAY, getJsonUtils().toJson(obj.getStringArray()));
        writer.addJson(LIST_OBJECT, getJsonUtils().toJson(obj.getListMyObjects()));
        writer.addJson(SET_OBJECT, getJsonUtils().toJson(obj.getSetMyObjects()));
    }
}
