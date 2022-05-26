package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.AdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void testJsonAdapterInstantiate_success() {
        adapterFactory.setPackageUri("com.pedrocosta.utils.jsonmanager");
        assert adapterFactory.create(MyObject.class) instanceof MyObjectAdapter;
    }

    @Test
    public void testJsonAdapterInstantiate_NullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            adapterFactory.create(MyObject.class);
        });
        String expectedMsg = "Could not get package uri. " +
                "Set project.package property or use setPackageUri method.";
        assertEquals(expectedMsg, exception.getMessage());
    }
}
