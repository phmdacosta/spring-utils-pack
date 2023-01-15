package com.pedrocosta.springutils.test;

import com.pedrocosta.springutils.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectUtilsTest {

    private MyChildObject _myChildObject;
    private MyBasicObject _myBasicObject;

    @BeforeEach
    public void setup() throws Exception {
        _myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        String[] strArr = {"str", "ttt"};
        _myChildObject = new MyChildObject(_myBasicObject, new MyInnerObject(strArr, 1.0D));
    }

    @Test
    public void test_equals() {
        assertTrue(ObjectUtils.equals(this._myBasicObject, this._myBasicObject));
        MyBasicObject myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject));
    }

    @Test
    public void test_notEquals() {
        MyBasicObject myBasicObject = new MyBasicObject("ghi", "def", 1, 2, 3, 4, true, false);
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject));
    }

    @Test
    public void testChild_equals() {
        assertTrue(ObjectUtils.equals(this._myChildObject, this._myChildObject));
        MyBasicObject myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        String[] strArr = {"str", "ttt"};
        MyChildObject myChildObject = new MyChildObject(myBasicObject, new MyInnerObject(strArr, 1.0D));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject));
    }

    @Test
    public void testChild_notEquals() {
        MyBasicObject myBasicObject = new MyBasicObject("ghi", "def", 1, 2, 3, 4, true, false);
        MyChildObject myChildObject = new MyChildObject(myBasicObject, null);
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject));

        myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        String[] strArr = {"int", "ttt"};
        myChildObject = new MyChildObject(myBasicObject, new MyInnerObject(strArr, 1.0D));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject));
    }

    @Test
    public void testField_equals() throws IllegalAccessException {
        MyBasicObject myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateString"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicString"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateInt"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicInt"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateInteger"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicInteger"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateBoolean"));
        assertTrue(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicBoolean"));
    }

    @Test
    public void testField_notEquals() throws IllegalAccessException {
        MyBasicObject myBasicObject = new MyBasicObject("ghi", "jkl", 5, 6, 7, 8, false, true);
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateString"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicString"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateInt"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicInt"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateInteger"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicInteger"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "privateBoolean"));
        assertFalse(ObjectUtils.equals(myBasicObject, this._myBasicObject, "publicBoolean"));
    }

    @Test
    public void testChildField_equals() throws IllegalAccessException {
        MyBasicObject myBasicObject = new MyBasicObject("abc", "def", 1, 2, 3, 4, true, false);
        String[] strArr = {"str", "ttt"};
        MyChildObject myChildObject = new MyChildObject(myBasicObject, new MyInnerObject(strArr, 1.0D));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "privateString"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "publicString"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "privateInt"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "publicInt"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "privateInteger"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "publicInteger"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "privateBoolean"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "publicBoolean"));
        assertTrue(ObjectUtils.equals(myChildObject, this._myChildObject, "myInnerObject"));
    }

    @Test
    public void testChildField_notEquals() throws IllegalAccessException {
        MyBasicObject myBasicObject = new MyBasicObject("ghi", "jkl", 5, 6, 7, 8, false, true);
        String[] strArr = {"int", "ttt"};
        MyChildObject myChildObject = new MyChildObject(myBasicObject, new MyInnerObject(strArr, 2.0D));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "privateString"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "publicString"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "privateInt"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "publicInt"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "privateInteger"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "publicInteger"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "privateBoolean"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "publicBoolean"));
        assertFalse(ObjectUtils.equals(myChildObject, this._myChildObject, "myInnerObject"));
    }

    public class MyBasicObject {
        private final String privateString;
        public String publicString;

        private final int privateInt;
        public int publicInt;

        private final Integer privateInteger;
        public Integer publicInteger;

        private final boolean privateBoolean;
        public boolean publicBoolean;

        public MyBasicObject(MyBasicObject myBasicObject) {
            this.privateString = myBasicObject.getPrivateString();
            this.publicString = myBasicObject.publicString;
            this.privateInt = myBasicObject.getPrivateInt();
            this.publicInt = myBasicObject.publicInt;
            this.privateInteger = myBasicObject.getPrivateInteger();
            this.publicInteger = myBasicObject.publicInteger;
            this.privateBoolean = myBasicObject.isPrivateBoolean();
            this.publicBoolean = myBasicObject.publicBoolean;
        }

        public MyBasicObject(String privateString, String publicString, int privateInt, int publicInt, Integer privateInteger, Integer publicInteger, boolean privateBoolean, boolean publicBoolean) {
            this.privateString = privateString;
            this.publicString = publicString;
            this.privateInt = privateInt;
            this.publicInt = publicInt;
            this.privateInteger = privateInteger;
            this.publicInteger = publicInteger;
            this.privateBoolean = privateBoolean;
            this.publicBoolean = publicBoolean;
        }

        public String getPrivateString() {
            return privateString;
        }

        public int getPrivateInt() {
            return privateInt;
        }

        public Integer getPrivateInteger() {
            return privateInteger;
        }

        public boolean isPrivateBoolean() {
            return privateBoolean;
        }
    }

    public class MyChildObject extends MyBasicObject {
        private final MyInnerObject myInnerObject;

        public MyChildObject(MyBasicObject myBasicObject, MyInnerObject myInnerObject) {
            super(myBasicObject);
            this.myInnerObject = myInnerObject;
        }

        public MyInnerObject getMyInnerObject() {
            return myInnerObject;
        }
    }

    public class MyInnerObject {
        private final String[] strArr;
        private final Double doubleVal;

        public MyInnerObject(String[] str, Double doubleVal) {
            this.strArr = str;
            this.doubleVal = doubleVal;
        }

        public String[] getStrArr() {
            return strArr;
        }

        public Double getDoubleVal() {
            return doubleVal;
        }
    }
}
