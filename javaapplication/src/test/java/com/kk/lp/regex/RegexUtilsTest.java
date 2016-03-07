package com.kk.lp.regex;

import org.junit.Test;

/**
 * Created by lipeng on 2016 3-4.
 */
public class RegexUtilsTest {

    @Test
    public void testCheckMobile() throws Exception {
        RegexUtils.checkMobile("1234678901");
    }
}