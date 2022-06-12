package com.pedrocosta.springutils.jsonmanager.obj;

import com.pedrocosta.springutils.DateUtils;
import com.pedrocosta.springutils.exception.NotSupportedException;
import com.pedrocosta.springutils.jsonmanager.adapter.WriteTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.stream.JsonWriter;

import java.io.IOException;

public class MyObjectWriteAdapter extends WriteTypeAdapter<MyObject> implements MyObjectAdapterNames {
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
        writer.add(BOOLEAN, obj.isBool());
        writer.addJson(CHILD, getJsonUtils().toJson(obj.getChild()));
    }
}
