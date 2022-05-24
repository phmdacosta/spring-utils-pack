package com.pedrocosta.utils.jsonmanager.obj;

import lombok.Data;

import java.util.Date;

@Data
public class MyObject {
    private long id;
    private String string;
    private Double doubl;
    private Date date;
}
