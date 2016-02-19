package com.kk.lp.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by lipeng on 1-27.
 */

public class ReflectionTest {

    @Test
    public void testField() {
        Object child = new Child();
        Field publicField = ReflectionUtils.getDeclaredField(child, "publicField");
        System.out.println(publicField.getName() + publicField.getType());
    }


}


