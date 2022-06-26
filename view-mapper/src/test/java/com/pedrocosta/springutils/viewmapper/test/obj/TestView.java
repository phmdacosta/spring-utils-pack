package com.pedrocosta.springutils.viewmapper.test.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestView {
    private String name;
    private boolean bool;
    private TestChildView child;
    private List<TestChildView> children = new ArrayList<>();
    private Map<Integer, TestChildView> childrenMap = new HashMap<>();

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

    public TestChildView getChild() {
        return child;
    }

    public void setChild(TestChildView child) {
        this.child = child;
    }

    public List<TestChildView> getChildren() {
        return children;
    }

    public void setChildren(List<TestChildView> children) {
        this.children = children;
    }

    public Map<Integer, TestChildView> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<Integer, TestChildView> childrenMap) {
        this.childrenMap = childrenMap;
    }
}
