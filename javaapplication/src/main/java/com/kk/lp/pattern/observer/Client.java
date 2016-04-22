package com.kk.lp.pattern.observer;

import com.kk.lp.pattern.observer.observer.PersonObserver;
import com.kk.lp.pattern.observer.subject.ISubject;
import com.kk.lp.pattern.observer.subject.Subject;

import java.util.Random;

/**
 * Created by lipeng on 2016 3-19.
 */
public class Client {
    public static void main(String[] args) {
        ISubject subject = new Subject();
        subject.add(new PersonObserver());
        int i = 0;
        while (i < 10){
            subject.setTemperature(new Random().nextInt(45));
            i++;
        }
    }
}
