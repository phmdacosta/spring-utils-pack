package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.AdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AdapterFactoryTest {

    private AdapterFactory adapterFactory;

    @BeforeEach
    public void setUp() {
        adapterFactory = new AdapterFactory();
    }

    @Test
    public void testJsonExchangeAdapterInstantiate() {
        assert adapterFactory.create(MyObject.class) instanceof MyObjectAdapter;
    }
}
