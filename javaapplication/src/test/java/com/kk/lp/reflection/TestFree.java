package com.kk.lp.reflection;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by lipeng on 1-29.
 */
public class TestFree {
    @Test
    public void testMethod() {
        Object child = new Child();
        Method publicMethod = ReflectionUtils.getDeclaredMethod(child, "publicMethod");
        System.out.println(publicMethod.getName());
    }

}
