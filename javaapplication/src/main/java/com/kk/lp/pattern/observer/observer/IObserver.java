package com.kk.lp.pattern.observer.observer;

import com.kk.lp.pattern.observer.subject.ISubject;

/**
 * Created by lipeng on 2016 3-19.
 */
public interface IObserver {

    void update(ISubject subject);
}
