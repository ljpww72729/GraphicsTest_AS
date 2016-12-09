package com.kk.lp.volley;

import java.io.UnsupportedEncodingException;

/**
 * Created by LinkedME06 on 16/10/30.
 */

public class Student {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public static void main(String[] args) {
        String messyName = "ä½¿ç\u0094¨æ·±åº¦é\u0093¾æ\u008E¥ä»£ç \u0081R";
        try {
            String str = new String(new String(messyName.getBytes("ISO-8859-1"), "GBK").getBytes("GBK"), "UTF-8");
            System.out.println("str=" + str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
