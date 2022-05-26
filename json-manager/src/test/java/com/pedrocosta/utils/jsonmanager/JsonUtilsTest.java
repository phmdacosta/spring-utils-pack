package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.AdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.MyObject;
import com.pedrocosta.utils.jsonmanager.obj.MyObjectExposeAnnot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {

    private MyObject myObjectNoExposeAnnot;
    private MyObjectExposeAnnot myObjectExposeAnnot;
    private JsonUtils jsonUtils;
    private final String expectedNoExpAnnot =  "{" +
            "\"id\":1," +
            "\"string\":\"My string\"," +
            "\"double\":2.5," +
            "\"date\":\"2022-05-10\"" +
            "}";

    @BeforeEach
    public void setUp() {
        jsonUtils = new JsonUtils(new AdapterFactory()
                .setPackageUri("com.pedrocosta.utils.jsonmanager"));

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        myObjectNoExposeAnnot = new MyObject();
        myObjectNoExposeAnnot.setId(1);
        myObjectNoExposeAnnot.setString("My string");
        myObjectNoExposeAnnot.setDoubl(2.5d);
        myObjectNoExposeAnnot.setDate(cal.getTime());

        myObjectExposeAnnot = new MyObjectExposeAnnot();
        myObjectExposeAnnot.setId(2);
        myObjectExposeAnnot.setString("My string for exposed annotation");
        myObjectExposeAnnot.setDoubl(1.55d);
        myObjectExposeAnnot.setDate(cal.getTime());
    }

    @AfterEach
    public void tearDown() {
        myObjectNoExposeAnnot = null;
        jsonUtils = null;
    }

    @Test
    public void testJsonSerialize_withAdapter() {
        String json = jsonUtils.toJson(myObjectNoExposeAnnot);
        assertEquals(expectedNoExpAnnot, json);
    }

    @Test
    public void testJsonDeserialize_withAdapter() {
        MyObject obj = jsonUtils.fromJson(expectedNoExpAnnot, MyObject.class);
        assertEquals(myObjectNoExposeAnnot, obj);
    }

    @Test
    public void testJsonSerialize_withExpose() {
        final String expectedExpAnnotSerialized =  "{" +
                "\"id\":2," +
                "\"string\":\"My string for exposed annotation\"," +
                "\"doubl\":1.55" +
                "}";
        jsonUtils.setUseExposedAnnotation(true);
        String json = jsonUtils.toJson(myObjectExposeAnnot);
        assertEquals(expectedExpAnnotSerialized, json);
    }

    @Test
    public void testJsonDeserialize_withExpose() {
        final String jsonExpAnnot =  "{" +
                "\"id\":2," +
                "\"string\":\"My string for exposed annotation\"," +
                "\"doubl\":1.55," +
                "\"date\":\"2022-05-10\"" +
                "}";
        jsonUtils.setUseExposedAnnotation(true);
        myObjectExposeAnnot.setDoubl(null);
        MyObjectExposeAnnot obj = jsonUtils.fromJson(jsonExpAnnot, MyObjectExposeAnnot.class);
        assertEquals(myObjectExposeAnnot, obj);
    }
}
