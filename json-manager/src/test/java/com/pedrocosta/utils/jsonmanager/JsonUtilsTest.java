package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.AdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {

    private MyObject myObject;
    private JsonUtils jsonUtils;
    private final String expected =  "{" +
            "\"id\":1," +
            "\"string\":\"My string\"," +
            "\"double\":2.5," +
            "\"date\":\"2022-05-10\"" +
            "}";

    @BeforeEach
    public void setUp() {
        jsonUtils = new JsonUtils(new AdapterFactory());
                //.setPackageUri("com.pedrocosta.utils.jsonmanager"));

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        myObject = new MyObject();
        myObject.setId(1);
        myObject.setString("My string");
        myObject.setDoubl(2.5d);
        myObject.setDate(cal.getTime());
    }

    @AfterEach
    public void tearDown() {
        myObject = null;
    }

    @Test
    public void testJsonSerialize() {
        String json = jsonUtils.toJson(myObject);
        assertEquals(expected, json);
    }

    @Test
    public void testJsonDeserialize() {
        MyObject obj = jsonUtils.fromJson(expected, MyObject.class);
        assertEquals(myObject, obj);
    }
}
