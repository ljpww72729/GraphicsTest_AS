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

    /**
     * 测试uri scheme表达式
     * @param uriScheme
     * @return
     */
    public static boolean regluarUriScheme(String uriScheme) {
        String rfc2396regex = "^(([a-zA-Z][a-zA-Z0-9\\+\\-\\.]*)://)((([^/?#]*))?([^?#]*)(\\?([^#]*))?)?(#(.*))?";
        String http_scheme_slashes = "^(https?://)/+(.*)";
        String all_schemes_pattern = "(?i)^(http|https|ftp|mms|rtsp|wais)://.*";
        if (uriScheme.matches(rfc2396regex)){
            return true;
        }
        return false;
    }
    public static void main(String args[]){
        System.out.println(checkMobile("12341281011"));
    }
}
