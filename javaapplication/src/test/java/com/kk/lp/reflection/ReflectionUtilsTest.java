package com.kk.lp.reflection;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by lipeng on 2016 3-19.
 */
public class ReflectionUtilsTest {
    Object child;

    @Before
    public void setUp() throws Exception {
        child = new Child();
    }

    @Test
    public void testGetDeclaredField() throws Exception {

        Field publicField = ReflectionUtils.getDeclaredField(child, "privateField");
        System.out.println(publicField.getName() + publicField.getType());
    }

    @Test
    public void testGetDeclaredMethod() throws Exception {

    }

    @Test
    public void testSetDeclaredField() throws Exception {
        Child child = ReflectionUtils.setDeclaredField(Child.class, "name");
        System.out.println(child.getName());

    }
}