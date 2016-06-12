package com.kk.lp.arithmetic;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by lipeng on 2016 6-8.
 */
public class LogicTest {

    public Logic logic;

    @Before
    public void setUp() throws Exception {
        logic = new Logic();
    }

    @Test
    public void testGetMinIndexOnlyOne() throws Exception {
        String[] arr = {"1", "2", "3", "4", "12", "asd", "ddd", "f", "d", "3", "r", "4", "1", "2"};
        Logic.ArrayEntry arrayEntry = logic.getMinIndexOnlyOne(arr);
        if (arrayEntry == null) {
            System.out.println(new String("没有这样的值".getBytes("utf-8"), Charset.forName("utf-8")));
        } else {
            System.out.println("最小下标：===" + arrayEntry.getIndex() + ",该下标对应的值===" + arr[arrayEntry.getIndex()]);
        }
    }
}