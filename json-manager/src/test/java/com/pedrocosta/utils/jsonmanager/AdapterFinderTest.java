package com.pedrocosta.utils.jsonmanager;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.AdapterFinder;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectAnnot;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectAnnotAdapter;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectFullAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AdapterFinderTest {
    @Test
    public void test_findOneAdapter() throws Exception {
        TypeAdapter<?> adapter = AdapterFinder.findAdapterByName(
                this.getClass().getPackage().getName() + ".obj.MyObjectFullAdapter");
        assert adapter instanceof MyObjectFullAdapter;
    }

    @Test
    public void test_findAllAdapters_annotated() throws Exception {
        List<TypeAdapter<MyObjectAnnot>> adapters = AdapterFinder
                .findAllAnnotated(this.getClass().getPackage().getName());
        assert !adapters.isEmpty();
        assert adapters.stream().findFirst().orElse(null) instanceof MyObjectAnnotAdapter;
    }
}
