package com.kk.lp.pattern.observer.subject;

import com.kk.lp.pattern.observer.observer.IObserver;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lipeng on 2016 3-19.
 */
public class Subject implements ISubject {

    private float temperature;
    private String warningLevel;
    private ArrayList<IObserver> list;

    public Subject() {
        list = new ArrayList<>();
    }

    @Override
    public boolean add(IObserver observer) {
        if (observer != null && list.contains(observer)) {
            return list.add(observer);
        }
        return false;
    }

    @Override
    public boolean remove(IObserver observer) {
        return list.remove(observer);
    }

    @Override
    public void notifyAllObserver() {
        Iterator<IObserver> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().update(this);
        }
    }

    @Override
    public void setTemperature(float temperature) {
        this.temperature = temperature;
        this.invoke();
    }

    private void invoke() {
        if (temperature >= 37) {
            warningLevel = "红色";
        } else if (temperature >= 35 && temperature < 37) {
            warningLevel = "黄色";
        }
        this.notifyAllObserver();
    }

    @Override
    public String temperatureReport() {
        return "温度：" + this.temperature;
    }
}
