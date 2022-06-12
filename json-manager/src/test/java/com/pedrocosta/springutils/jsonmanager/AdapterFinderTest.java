package com.pedrocosta.springutils.jsonmanager;

import com.google.gson.TypeAdapter;
import com.pedrocosta.springutils.jsonmanager.adapter.AdapterFinder;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.obj.MyObjectAnnot;
import com.pedrocosta.springutils.jsonmanager.obj.MyObjectAnnotAdapter;
import com.pedrocosta.springutils.jsonmanager.obj.MyObjectFullAdapter;
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
        List<UtilsTypeAdapter<MyObjectAnnot>> adapters = AdapterFinder
                .findAllAnnotated(this.getClass().getPackage().getName());
        assert !adapters.isEmpty();
        assert adapters.stream().findFirst().orElse(null) instanceof MyObjectAnnotAdapter;
    }
}
