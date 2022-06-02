package com.pedrocosta.utils.jsonmanager.obj;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class MyObject implements Serializable {
    private long id;
    private String string;
    private Double doubl;
    private Date date;
    private List<String> listStrings;
    private Set<String> setStrings;
    private String[] stringArray;
    private List<MyObject> listMyObjects;
    private Set<MyObject> setMyObjects;
}
