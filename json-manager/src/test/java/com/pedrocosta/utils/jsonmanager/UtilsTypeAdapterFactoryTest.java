package com.pedrocosta.utils.jsonmanager;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtilsTypeAdapterFactoryTest {
    private UtilsTypeAdapterFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new UtilsTypeAdapterFactory();
    }

    @Test
    public void test_AdapterCreation_preloadAdapters() {
        factory.addAdapter(MyObject.class, MyObjectReadAdapter.class);
        TypeAdapter<MyObject> adapter = factory.create(MyObject.class);
        assert adapter != null;
        assert adapter instanceof MyObjectReadAdapter;
    }

    @Test
    public void test_AdapterCreation_withAdapterProperties() {
        TypeAdapter<MyObject> adapter = factory.create(MyObject.class);
        assert adapter != null;
        assert adapter instanceof MyObjectFullAdapter;
    }

    @Test
    public void test_AdapterCreation_withAnnotation() {
        factory.useAnnotation(true);
        TypeAdapter<MyObjectAnnot> adapter = factory.create(MyObjectAnnot.class);
        assert adapter != null;
        assert adapter instanceof MyObjectAnnotAdapter;
    }
}
