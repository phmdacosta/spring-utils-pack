package com.pedrocosta.springutils.test.utils;

import java.util.Objects;

public class ModelTest {
    private String str;
    private Integer integerNum;
    private Double doubleNum;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Integer getIntegerNum() {
        return integerNum;
    }

    public void setIntegerNum(Integer integerNum) {
        this.integerNum = integerNum;
    }

    public Double getDoubleNum() {
        return doubleNum;
    }

    public void setDoubleNum(Double doubleNum) {
        this.doubleNum = doubleNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelTest)) return false;
        ModelTest modelTest = (ModelTest) o;
        return Objects.equals(getStr(), modelTest.getStr()) && Objects.equals(getIntegerNum(), modelTest.getIntegerNum()) && Objects.equals(getDoubleNum(), modelTest.getDoubleNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStr(), getIntegerNum(), getDoubleNum());
    }
}
