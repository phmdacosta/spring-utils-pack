package com.pedrocosta.springutils.viewmapper.test.obj;

import com.pedrocosta.springutils.viewmapper.annotation.MappingCollection;
import com.pedrocosta.springutils.viewmapper.annotation.MappingField;
import com.pedrocosta.springutils.viewmapper.annotation.MappingMap;
import com.pedrocosta.springutils.viewmapper.annotation.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@View
public class TestViewAnnot {
    @MappingField(name = "name")
    private String viewName;
    private boolean bool;
    private TestChildView childView;
    @MappingCollection(name = "children", resultElementClass = TestChildView.class)
    private List<TestChildView> childrenView = new ArrayList<>();
    @MappingMap(name = "childrenMap", resultValueClass = TestChildView.class)
    private Map<Integer, TestChildView> childrenViewMap = new HashMap<>();

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public TestChildView getChildView() {
        return childView;
    }

    @MappingField(name = "child")
    public void setChildView(TestChildView childView) {
        this.childView = childView;
    }

    public List<TestChildView> getChildrenView() {
        return childrenView;
    }

    public void setChildrenView(List<TestChildView> childrenView) {
        this.childrenView = childrenView;
    }

    public Map<Integer, TestChildView> getChildrenViewMap() {
        return childrenViewMap;
    }

    public void setChildrenViewMap(Map<Integer, TestChildView> childrenViewMap) {
        this.childrenViewMap = childrenViewMap;
    }
}
