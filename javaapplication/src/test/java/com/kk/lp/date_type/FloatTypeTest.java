package com.kk.lp.date_type;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by lipeng on 2016 5-23.
 */
public class FloatTypeTest {

    FloatType floatType;

    @Before
    public void setUp() throws Exception {
        floatType = new FloatType();
    }

    @Test
    public void testCompareFloat() throws Exception {
        floatType.compareFloat();
    }
}