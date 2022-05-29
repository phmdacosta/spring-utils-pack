package com.pedrocosta.utils.jsonmanager;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.TypeAdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class TypeAdapterFactoryTest {

    private TypeAdapterFactory typeAdapterFactory;

    @BeforeEach
    public void setUp() {
        typeAdapterFactory = new TypeAdapterFactory();
    }

    @Test
    public void testJsonTypeAdapterInstantiate_success() {
        typeAdapterFactory.setPackageUri("com.pedrocosta.utils.jsonmanager");
        TypeAdapter<?> typeAdapter = typeAdapterFactory.create(MyObject.class, "Type");
        assert typeAdapter != null;
        assert typeAdapter instanceof MyObjectTypeAdapter;
    }

    @Test
    public void testJsonTypeAdapterInstantiate_NullPointerException() {
        typeAdapterFactory.setPackageUri(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            typeAdapterFactory.create(MyObject.class);
        });
        String expectedMsg = "Could not get package uri. " +
                "Set project.package property or use setPackageUri method.";
        assertEquals(expectedMsg, exception.getMessage());
    }
}
