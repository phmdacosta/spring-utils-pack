package com.pedrocosta.utils.jsonmanager;

import com.google.gson.TypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.JsonAdapterFactory;
import com.pedrocosta.utils.jsonmanager.adapter.JsonTypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.TypeAdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonAdapterFactoryTest {

    private TypeAdapterFactory jsonAdapterFactory;

    @BeforeEach
    public void setUp() {
        jsonAdapterFactory = new JsonAdapterFactory();
        jsonAdapterFactory.setPackageUri("com.pedrocosta.utils.jsonmanager");
    }

    @Test
    public void testJsonReadAdapterInstantiate_success() {
        TypeAdapter<?> typeAdapter = jsonAdapterFactory.create(MyObject.class, "Read");
        assert typeAdapter != null;
        assert ((JsonTypeAdapter<?>) typeAdapter).getReadAdapter() instanceof MyObjectReadAdapter;
    }

    @Test
    public void testJsonWriteAdapterInstantiate_success() {
        TypeAdapter<?> typeAdapter = jsonAdapterFactory.create(MyObject.class, "Write") ;
        assert typeAdapter != null;
        assert ((JsonTypeAdapter<?>) typeAdapter).getWriteAdapter() instanceof MyObjectWriteAdapter;
    }

    @Test
    public void testJsonReadAndWriteAdapterInstantiate_success() {
        TypeAdapter<?> typeAdapter = jsonAdapterFactory.create(MyObject.class, "Full");
        assert typeAdapter != null;
        assert ((JsonTypeAdapter<?>) typeAdapter).getReadAdapter() instanceof MyObjectFullAdapter;
        assert ((JsonTypeAdapter<?>) typeAdapter).getWriteAdapter() instanceof MyObjectFullAdapter;
    }

    @Test
    public void testJsonAdapterInstantiate_NullPointerException() {
        jsonAdapterFactory.setPackageUri(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            jsonAdapterFactory.create(MyObject.class);
        });
        String expectedMsg = "Could not get package uri. " +
                "Set project.package property or use setPackageUri method.";
        assertEquals(expectedMsg, exception.getMessage());
    }
}
