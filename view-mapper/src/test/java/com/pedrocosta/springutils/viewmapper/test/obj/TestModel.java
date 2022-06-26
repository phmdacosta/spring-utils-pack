package com.pedrocosta.springutils.viewmapper.test.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestModel {
    private long id;
    private String name;
    private boolean bool;
    private TestChildModel child;
    private List<TestChildModel> children = new ArrayList<>();
    private Map<Integer, TestChildModel> childrenMap = new HashMap<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public TestChildModel getChild() {
        return child;
    }

    public void setChild(TestChildModel child) {
        this.child = child;
    }

    public List<TestChildModel> getChildren() {
        return children;
    }

    public void setChildren(List<TestChildModel> children) {
        this.children = children;
    }

    public Map<Integer, TestChildModel> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<Integer, TestChildModel> childrenMap) {
        this.childrenMap = childrenMap;
    }
}
