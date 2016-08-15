package com.kk.lp.inter;

import org.junit.Before;
import org.junit.Test;

import static com.kk.lp.inter.InterfaceInstance.*;
import static org.junit.Assert.*;

/**
 * Created by LinkedME06 on 16/7/11.
 */
public class InterfaceInstanceTest {

    private InterfaceInstance interfaceInstance;

    @Before
    public void setUp() throws Exception {
        interfaceInstance = new InterfaceInstance();
    }

    @Test
    public void testGetMessage() throws Exception {
        Callback callback = new Callback() {
            @Override
            public void print(String message) {
                super.print(message);
                System.out.println("message=====" + message);
            }
        };
        interfaceInstance.getMessage(callback, "1");
        interfaceInstance.getMessage(callback, "2");
        interfaceInstance.getMessage(callback, "3");
        interfaceInstance.getMessage(callback, "4");
    }
}