package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.AdapterProperties;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AdapterPropertiesTest {

    @Test
    public void testGetAllProperties() {
        List<String> properties = AdapterProperties.getAll();
        assert !properties.isEmpty();
    }
}
