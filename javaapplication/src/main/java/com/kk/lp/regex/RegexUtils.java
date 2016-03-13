package com.kk.lp.regex;

import java.util.regex.Pattern;

/**
 * Created by lipeng on 2016 3-4.
 */
public class RegexUtils {

    /**
     * 匹配以1开头的11位数字电话号码
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        String regex = "^1\\d{10}$";
        return Pattern.matches(regex,mobile);
    }

    public static void main(String args[]){
        System.out.println(checkMobile("12341281011"));
    }
}
