package com.kk.lp.exception;

/**
 * Created by LinkedME06 on 06/01/2017.
 */

public class TryException {
    public static void main(String[] args) {
        try {
            int a = 3 / 0;
            System.out.println("abc");
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
