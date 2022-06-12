package com.pedrocosta.springutils.jsonmanager.obj;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class MyObjectExposeAnnot {
    @Expose
    private long id;
    @Expose
    private String string;
    @Expose(deserialize = false)
    private Double doubl;
    @Expose(serialize = false)
    private Date date;
//    @Expose
    private List<String> listStrings;
    @Expose
    private Set<String> setStrings;
    @Expose
    private String[] stringArray;
    @Expose
    private List<MyObjectExposeAnnot> listMyObjects;
//    @Expose
    private Set<MyObjectExposeAnnot> setMyObjects;
}
