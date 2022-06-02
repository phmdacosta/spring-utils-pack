package com.pedrocosta.utils.jsonmanager;

import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.utils.jsonmanager.obj.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {
    private MyObject myObjectNoExposeAnnot;
    private MyObjectExposeAnnot myObjectExposeAnnot;
    private JsonUtils jsonUtils;
    private final String expectedNoExpAnnot =  "{" +
            "\"id\":1" +
            ",\"string\":\"My string\"" +
            ",\"double\":2.5" +
            ",\"date\":\"2022-05-10\"" +
            ",\"listStrings\":[\"abc\",\"def\",\"ghi\"]" +
            ",\"setStrings\":[\"jkl\"]" +
            ",\"stringArray\":[\"mno\",\"pqr\",\"stu\"]" +
            ",\"listMyObjects\":[" +
                                    "{\"" +
                                        "id\":5," +
                                        "\"string\":\"My object 1 to list\"" +
                                    "}," +
                                    "{" +
                                        "\"id\":6," +
                                        "\"string\":\"My object 2 to list\"" +
                                    "}" +
                                "]" +
            ",\"setMyObjects\":[" +
                                "{" +
                                    "\"id\":6," +
                                    "\"string\":\"My object 2 to list\"" +
                                "}]"+
            "}";

    @BeforeEach
    public void setUp() {
        UtilsTypeAdapterFactory adapterFactory =
                new UtilsTypeAdapterFactory();//.setPackageUri(this.getClass().getPackage().getName());
        jsonUtils = new JsonUtils().setTypeAdapterFactory(adapterFactory);

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        myObjectNoExposeAnnot = new MyObject();
        myObjectNoExposeAnnot.setId(1);
        myObjectNoExposeAnnot.setString("My string");
        myObjectNoExposeAnnot.setDoubl(2.5d);
        myObjectNoExposeAnnot.setDate(cal.getTime());
        myObjectNoExposeAnnot.setListStrings(Arrays.asList("abc", "def", "ghi"));
        myObjectNoExposeAnnot.setSetStrings(new HashSet<>(Collections.singletonList("jkl")));
        myObjectNoExposeAnnot.setStringArray(new String[]{"mno", "pqr", "stu"});
        MyObject myObject1 = new MyObject();
        myObject1.setId(5);
        myObject1.setString("My object 1 to list");
        MyObject myObject2 = new MyObject();
        myObject2.setId(6);
        myObject2.setString("My object 2 to list");
        myObjectNoExposeAnnot.setListMyObjects(Arrays.asList(myObject1, myObject2));
        myObjectNoExposeAnnot.setSetMyObjects(new HashSet<>(Collections.singletonList(myObject2)));


        myObjectExposeAnnot = new MyObjectExposeAnnot();
        myObjectExposeAnnot.setId(2);
        myObjectExposeAnnot.setString("My string for exposed annotation");
        myObjectExposeAnnot.setDoubl(1.55d);
        myObjectExposeAnnot.setDate(cal.getTime());
        myObjectExposeAnnot.setSetStrings(new HashSet<>(Collections.singletonList("jkl")));
        myObjectExposeAnnot.setStringArray(new String[]{"mno", "pqr", "stu"});
        MyObjectExposeAnnot myObject3 = new MyObjectExposeAnnot();
        myObject3.setId(5);
        myObject3.setString("My object 1 to list");
        MyObjectExposeAnnot myObject4 = new MyObjectExposeAnnot();
        myObject4.setId(6);
        myObject4.setString("My object 2 to list");
        myObjectExposeAnnot.setListMyObjects(Arrays.asList(myObject3, myObject4));
    }

    @Test
    public void testJsonSerialize_withReadAndWriteAdapter() {
        String json = jsonUtils.toJson(myObjectNoExposeAnnot);
        assertEquals(expectedNoExpAnnot, json);
    }

    @Test
    public void testJsonSerialize_withWriteAdapter() {
        String json = jsonUtils.toJson(myObjectNoExposeAnnot, new MyObjectWriteAdapter());
        assertEquals(expectedNoExpAnnot, json);
    }

    @Test
    public void testJsonDeserialize_withReadAndWriteAdapter() {
        MyObject obj = jsonUtils.fromJson(expectedNoExpAnnot, MyObject.class);
        assertEquals(myObjectNoExposeAnnot, obj);
    }

    @Test
    public void testJsonDeserialize_withReadAdapter() {
        MyObject obj = jsonUtils.fromJson(expectedNoExpAnnot, MyObject.class, new MyObjectReadAdapter());
        assertEquals(myObjectNoExposeAnnot, obj);
    }

    @Test
    public void testJsonSerialize_withExpose() {
        final String expectedExpAnnotSerialized =  "{" +
                "\"id\":2," +
                "\"string\":\"My string for exposed annotation\"," +
                "\"doubl\":1.55" +
                ",\"set_strings\":[\"jkl\"]" +
                ",\"string_array\":[\"mno\",\"pqr\",\"stu\"]" +
                ",\"list_my_objects\":[" +
                    "{\"" +
                        "id\":5," +
                        "\"string\":\"My object 1 to list\"" +
                    "}," +
                    "{" +
                        "\"id\":6," +
                        "\"string\":\"My object 2 to list\"" +
                    "}" +
                "]" +
                "}";
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
                ",\"set_strings\":[\"jkl\"]" +
                ",\"string_array\":[\"mno\",\"pqr\",\"stu\"]" +
                ",\"list_my_objects\":[" +
                    "{\"" +
                        "id\":5," +
                        "\"string\":\"My object 1 to list\"" +
                    "}," +
                    "{" +
                        "\"id\":6," +
                        "\"string\":\"My object 2 to list\"" +
                    "}" +
                "]" +
                "}";
        myObjectExposeAnnot.setDoubl(null);
        MyObjectExposeAnnot obj = jsonUtils.fromJson(jsonExpAnnot, MyObjectExposeAnnot.class);
        assertEquals(myObjectExposeAnnot, obj);
    }
}
