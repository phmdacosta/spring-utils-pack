package com.pedrocosta.springutils.test;

import com.pedrocosta.springutils.collection.CollectionUtils;
import com.pedrocosta.springutils.test.models.ModelTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilsTest {

    @Test
    public void testSortListByStringAsc() {
        List<ModelTest> list = new ArrayList<>();
        list.add(createModelTest("Test 01", 1156, 5.5));
        list.add(createModelTest("Test 02", 15, 80.6));
        list.add(createModelTest("Test 03", 94, 158.5312));

        CollectionUtils.sort(list, "str", true, false);

        assertEquals("Test 01", list.get(0).getStr());
        assertEquals("Test 03", list.get(list.size() - 1).getStr());
    }

    @Test
    public void testSortListByStringDesc() {
        List<ModelTest> list = new ArrayList<>();
        list.add(createModelTest("Test 01", 1156, 5.5));
        list.add(createModelTest("Test 02", 15, 80.6));
        list.add(createModelTest("Test 03", 94, 158.5312));

        CollectionUtils.sort(list, "str", false, false);

        assertEquals("Test 03", list.get(0).getStr());
        assertEquals("Test 01", list.get(list.size() - 1).getStr());
    }

    private ModelTest createModelTest(String str, Integer integerNum, Double doubleNum) {
        ModelTest model = new ModelTest();
        model.setStr(str);
        model.setIntegerNum(integerNum);
        model.setDoubleNum(doubleNum);
        return model;
    }
}
