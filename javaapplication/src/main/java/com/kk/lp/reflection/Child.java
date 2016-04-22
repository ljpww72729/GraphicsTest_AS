package com.kk.lp.reflection;

/**
 * 子类
 * Created by lipeng on 1-27.
 */
public class Child extends Parent {
    public Child(){
        System.setSecurityManager(new SecurityManager());
    }
    private String name;

    public String getName() {
        return name;
    }
}
