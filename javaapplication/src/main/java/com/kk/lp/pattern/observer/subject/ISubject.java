package com.kk.lp.pattern.observer.subject;

import com.kk.lp.pattern.observer.observer.IObserver;

/**
 * Created by lipeng on 2016 3-19.
 */
public interface ISubject {
    boolean add(IObserver observer);
    boolean remove(IObserver observer);
    void notifyAllObserver();
    void setTemperature(float temperature);
    String temperatureReport();
}
