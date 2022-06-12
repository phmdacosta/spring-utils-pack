package com.pedrocosta.springutils.jsonmanager;

import com.pedrocosta.springutils.jsonmanager.adapter.AdapterProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AdapterPropertiesTest {

    @Test
    public void testGetAllProperties() {
        List<String> properties = AdapterProperties.getAll();
        assert !properties.isEmpty();
    }
}
