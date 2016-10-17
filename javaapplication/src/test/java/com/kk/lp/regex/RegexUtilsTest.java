package com.kk.lp.regex;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lipeng on 2016 3-4.
 */
public class RegexUtilsTest {
    @Test
    public void regluarUriScheme1() throws Exception {

    }

    @Test
    public void regluarUriScheme() throws Exception {
        Assert.assertTrue("uri scheme 格式不对!", RegexUtils.regluarUriScheme("intent:///#Intent;scheme=zxing;package=com.google.zxing.client.android;end"));
    }

    @Test
    public void testCheckMobile() throws Exception {
        Assert.assertTrue(RegexUtils.checkMobile("1234678011"));
    }
}