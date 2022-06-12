package com.pedrocosta.springutils.test;

import com.pedrocosta.springutils.NumberUtils;
import org.junit.jupiter.api.Test;

public class NumberUtilsTest {

    @Test
    public void testIsDouble() throws Exception {
        assert NumberUtils.isDouble(1.2);
        assert !NumberUtils.isDouble(1);
        assert !NumberUtils.isDouble(1.0);
    }

    @Test
    public void testValueOf() {
        String str = "1";
        Long l = NumberUtils.valueOf(str, Long.class);
        assert l == 1L;
        Integer i = NumberUtils.valueOf(str, Integer.class);
        assert i == 1;
        Short s = NumberUtils.valueOf(str, Short.class);
        assert s == 1;
        Double d = NumberUtils.valueOf(str, Double.class);
        assert d == 1D;
        Float f = NumberUtils.valueOf(str, Float.class);
        assert f == 1.0;
    }

    @Test
    public void testIsNumberClass() {
        assert NumberUtils.isNumberClass(Integer.class);
        assert NumberUtils.isNumberClass(Long.class);
        assert NumberUtils.isNumberClass(Short.class);
        assert NumberUtils.isNumberClass(Double.class);
        assert NumberUtils.isNumberClass(Float.class);
        assert !NumberUtils.isNumberClass(String.class);
    }
}
