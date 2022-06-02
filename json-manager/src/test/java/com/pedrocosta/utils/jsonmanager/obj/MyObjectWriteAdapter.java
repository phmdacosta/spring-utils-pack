package com.pedrocosta.utils.jsonmanager.obj;

import com.pedrocosta.utils.DateUtils;
import com.pedrocosta.utils.exception.NotSupportedException;
import com.pedrocosta.utils.jsonmanager.adapter.WriteTypeAdapter;
import com.pedrocosta.utils.jsonmanager.stream.JsonWriter;

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
    }
}
