package com.kk.lp.pattern.observer.observer;

import com.kk.lp.pattern.observer.subject.ISubject;

/**
 * Created by lipeng on 2016 3-19.
 */
public class PersonObserver implements IObserver {
    @Override
    public void update(ISubject subject) {
        System.out.println("个人收到的高温预警信息为：" + subject.temperatureReport());
    }
}
