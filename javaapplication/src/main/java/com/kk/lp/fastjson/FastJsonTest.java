package com.kk.lp.fastjson;

import com.alibaba.fastjson.JSON;

/**
 * Created by lipeng on 2-22.
 */
public class FastJsonTest {
    public static void main(String args[]){
        String json = "{\"NAME\":\"lipeng\", \"Age\":\"12\", \"FirstName\":\"Li\"}";
        Person person = JSON.parseObject(json, Person.class);
        System.out.println(person.name);
        System.out.println(person.age);
        System.out.println(person.firstName);
    }

}
