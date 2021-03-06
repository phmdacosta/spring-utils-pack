package com.pedrocosta.springutils.viewmapper.test;

import com.pedrocosta.springutils.viewmapper.ViewMapper;
import com.pedrocosta.springutils.viewmapper.test.obj.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class ViewMapperTest {

    private ViewMapper viewMapper;

    @BeforeEach
    public void setUp() {
        this.viewMapper = new ViewMapper();
    }

    @Test
    public void test_viewMapping_commonFields_success() throws Exception {
        TestModel model = new TestModel();
        model.setId(1);
        model.setName("Model Name");
        model.setBool(true);

        TestChildModel child = new TestChildModel();
        child.setName("Child Name");
        model.setChild(child);

        TestView view = viewMapper.map(model, TestView.class);
        assertEquals(view.getName(), model.getName());
        assertEquals(view.isBool(), model.isBool());
        assertEquals(view.getChild().getName(), model.getChild().getName());
    }

    @Test
    public void test_modelMapping_commonFields_success() throws Exception {
        TestView view = new TestView();
        view.setName("Model Name");
        view.setBool(true);

        TestChildView child = new TestChildView();
        child.setName("Child Name");
        view.setChild(child);

        TestModel model = viewMapper.map(view, TestModel.class);
        assertEquals(model.getName(), view.getName());
        assertEquals(model.isBool(), view.isBool());
        assertEquals(model.getChild().getName(), view.getChild().getName());
    }

    @Test
    public void test_bothMapping_collections_success() throws Exception {
        //Model
        TestModel model = new TestModel();
        TestChildModel child1 = new TestChildModel();
        TestChildModel child2 = new TestChildModel();
        child1.setName("Child Name 1");
        child2.setName("Child Name 2");
        model.getChildren().add(child1);
        model.getChildren().add(child2);
        model.getChildrenMap().put(1, child1);
        model.getChildrenMap().put(2, child2);

        //View
        TestView view = new TestView();
        TestChildView childView1 = new TestChildView();
        TestChildView childView2 = new TestChildView();
        childView1.setName("Child Name 1");
        childView2.setName("Child Name 2");
        view.getChildren().add(childView1);
        view.getChildren().add(childView2);
        view.getChildrenMap().put(1, childView1);
        view.getChildrenMap().put(2, childView1);

        //Mapping
        TestView mappedView = viewMapper.map(model, TestView.class);
        TestModel mappedModel = viewMapper.map(view, TestModel.class);

        //Assertions
        for (TestChildView mappedChildView : mappedView.getChildren()) {
            Collection<?> found = model.getChildren().stream().filter(
                    childModel -> childModel.getName().equals(mappedChildView.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }

        for (TestChildModel mappedChildModel : mappedModel.getChildren()) {
            Collection<?> found = view.getChildren().stream().filter(
                    _child -> _child.getName().equals(mappedChildModel.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }

        for (Map.Entry<Integer, TestChildView> entry : mappedView.getChildrenMap().entrySet()) {
            Map.Entry<Integer, TestChildModel> found = model.getChildrenMap().entrySet().stream()
                    .filter(_entry -> _entry.getValue().getName().equals(entry.getValue().getName()))
                    .findFirst().orElse(null);
            assertNotNull(found);
        }

        for (Map.Entry<Integer, TestChildModel> entry : mappedModel.getChildrenMap().entrySet()) {
            Map.Entry<Integer, TestChildView> found = view.getChildrenMap().entrySet().stream()
                    .filter(_entry -> _entry.getValue().getName().equals(entry.getValue().getName()))
                    .findFirst().orElse(null);
            assertNotNull(found);
        }
    }

    @Test
    public void test_viewMapping_annotation_success() throws Exception {
        TestModel model = new TestModel();
        model.setId(1);
        model.setName("Model Name");
        model.setBool(true);

        TestChildModel child = new TestChildModel();
        child.setName("Child Name");
        model.setChild(child);

        TestChildModel child1 = new TestChildModel();
        TestChildModel child2 = new TestChildModel();
        child1.setName("Child Name 1");
        child2.setName("Child Name 2");
        model.getChildren().add(child1);
        model.getChildren().add(child2);
        model.getChildrenMap().put(1, child1);
        model.getChildrenMap().put(2, child2);

        TestViewAnnot view = viewMapper.map(model, TestViewAnnot.class);
        assertEquals(view.getViewName(), model.getName());
        assertEquals(view.isBool(), model.isBool());
        assertEquals(view.getChildView().getName(), model.getChild().getName());
        for (TestChildView childView : view.getChildrenView()) {
            Collection<?> found = model.getChildren().stream().filter(
                    childModel -> childModel.getName().equals(childView.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
    }

    @Test
    public void test_modelMapping_annotation_success() throws Exception {
        TestViewAnnot view = new TestViewAnnot();
        view.setViewName("Model Name");
        view.setBool(true);

        TestChildView child = new TestChildView();
        child.setName("Child Name");
        view.setChildView(child);

        TestChildView child1 = new TestChildView();
        TestChildView child2 = new TestChildView();
        child1.setName("Child Name 1");
        child2.setName("Child Name 2");
        view.getChildrenView().add(child1);
        view.getChildrenView().add(child2);
        view.getChildrenViewMap().put(1, child1);
        view.getChildrenViewMap().put(2, child2);

        TestModel model = viewMapper.map(view, TestModel.class);
        assertEquals(model.getName(), view.getViewName());
        assertEquals(model.isBool(), view.isBool());
        assertEquals(model.getChild().getName(), view.getChildView().getName());
        for (TestChildModel childModel : model.getChildren()) {
            Collection<?> found = view.getChildrenView().stream().filter(
                    childView -> childView.getName().equals(childModel.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }
    }

    @Test
    public void test_bothMapping_annotation_collections_success() throws Exception {
        //Model
        TestModel model = new TestModel();
        TestChildModel child1 = new TestChildModel();
        TestChildModel child2 = new TestChildModel();
        child1.setName("Child Name 1");
        child2.setName("Child Name 2");
        model.getChildren().add(child1);
        model.getChildren().add(child2);
        model.getChildrenMap().put(1, child1);
        model.getChildrenMap().put(2, child2);

        //View
        TestViewAnnot view = new TestViewAnnot();
        TestChildView childView1 = new TestChildView();
        TestChildView childView2 = new TestChildView();
        childView1.setName("Child Name 1");
        childView2.setName("Child Name 2");
        view.getChildrenView().add(childView1);
        view.getChildrenView().add(childView2);
        view.getChildrenViewMap().put(1, childView1);
        view.getChildrenViewMap().put(2, childView1);

        //Mapping
        TestViewAnnot mappedView = viewMapper.map(model, TestViewAnnot.class);
        TestModel mappedModel = viewMapper.map(view, TestModel.class);

        //Assertions
        for (TestChildView mappedChildView : mappedView.getChildrenView()) {
            Collection<?> found = model.getChildren().stream().filter(
                    childModel -> childModel.getName().equals(mappedChildView.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }

        for (TestChildModel mappedChildModel : mappedModel.getChildren()) {
            Collection<?> found = view.getChildrenView().stream().filter(
                    _child -> _child.getName().equals(mappedChildModel.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }

        for (Map.Entry<Integer, TestChildView> entry : mappedView.getChildrenViewMap().entrySet()) {
            Map.Entry<Integer, TestChildModel> found = model.getChildrenMap().entrySet().stream()
                    .filter(_entry -> _entry.getValue().getName().equals(entry.getValue().getName()))
                    .findFirst().orElse(null);
            assertNotNull(found);
        }

        for (Map.Entry<Integer, TestChildModel> entry : mappedModel.getChildrenMap().entrySet()) {
            Map.Entry<Integer, TestChildView> found = view.getChildrenViewMap().entrySet().stream()
                    .filter(_entry -> _entry.getValue().getName().equals(entry.getValue().getName()))
                    .findFirst().orElse(null);
            assertNotNull(found);
        }
    }

    @Test
    public void test_bothMapping_sameClasses_success() throws Exception {
        //Model
        TestModel model = new TestModel();
        model.setId(1);
        model.setName("Model Name");
        model.setBool(true);

        TestChildModel child = new TestChildModel();
        child.setName("Child Name");
        model.setChild(child);

        TestChildModel child1 = new TestChildModel();
        TestChildModel child2 = new TestChildModel();
        child1.setName("Child Name 1");
        child2.setName("Child Name 2");
        model.getChildren().add(child1);
        model.getChildren().add(child2);
        model.getChildrenMap().put(1, child1);
        model.getChildrenMap().put(2, child2);

        //Mapping
        TestModel mapped = viewMapper.map(model, TestModel.class);

        //Assertions
        assertEquals(model.getName(), mapped.getName());
        assertEquals(model.isBool(), mapped.isBool());
        assertEquals(model.getChild().getName(), mapped.getChild().getName());

        for (TestChildModel mappedChildModel : mapped.getChildren()) {
            Collection<?> found = model.getChildren().stream().filter(
                    _child -> _child.getName().equals(mappedChildModel.getName())).collect(Collectors.toList());
            assertFalse(found.isEmpty());
        }

        for (Map.Entry<Integer, TestChildModel> entry : mapped.getChildrenMap().entrySet()) {
            Map.Entry<Integer, TestChildModel> found = model.getChildrenMap().entrySet().stream()
                    .filter(_entry -> _entry.getValue().getName().equals(entry.getValue().getName()))
                    .findFirst().orElse(null);
            assertNotNull(found);
        }
    }
}
