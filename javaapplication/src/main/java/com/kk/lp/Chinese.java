package com.kk.lp;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


/**
 * Created by LinkedME06 on 26/02/2017.
 */

public class Chinese {
    public static void main(String[] args) {

        String messyName = "å\u008F‚æ•°";
        try {
            String str = new String(new String(messyName.getBytes("ISO-8859-1"), "GBK").getBytes("GBK"), "UTF-8");
            System.out.println(str);
            Base64
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
