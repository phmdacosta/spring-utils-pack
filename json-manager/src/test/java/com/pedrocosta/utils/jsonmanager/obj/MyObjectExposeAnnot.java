package com.pedrocosta.utils.jsonmanager.obj;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Date;

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
}
