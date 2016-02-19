package com.kk.lp.reflection;

/**
 * 父类
 * Created by lipeng on 1-27.
 */
public class Parent {

    String defaultField = "defaultField";
    private String privateField = "privateField";
    protected String protectedField = "protectedField";
    public String publicField = "publicField";

    void defaultMethod() {
        System.out.println("defaultMethod...");
    }

    private void privateMethod() {
        System.out.println("privateMethod...");
    }

    protected void protectedMethod() {
        System.out.println("protectedMethod...");
    }

    public void publicMethod() {
        System.out.println("publicMethod...");
    }

    public void publicMethod(String arg1, boolean arg2) {
        System.out.println("publicMethod...with two parameter");
    }

//    public static void main(String args[]) {
//        System.out.println("abcd");
//    }
}
