package com.kk.lp.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.util.ArrayDeque;
import java.util.concurrent.Executor;

/**
 * Created by lipeng on 2016 3-17.
 */
public class CHanderThread extends HandlerThread {
    public CHanderThread(String name) {
        this(name, Process.THREAD_PRIORITY_BACKGROUND);
    }

    public CHanderThread(String name, int priority) {
        super(name, priority);
    }

    @Override
    public void run() {

        super.run();
    }

    static class LooperThread extends Thread {
              public Handler mHandler;

                      public void run() {
                      Looper.prepare();

                      mHandler = new Handler() {
                              public void handleMessage(Message msg) {
                                      // process incoming messages here
                                  }
                          };

                      Looper.loop();
                  }
          }
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> tasks = new ArrayDeque<Runnable>();
        final Executor executor;
        Runnable active;

        SerialExecutor(Executor executor) {
            this.executor = executor;
        }

        public synchronized void execute(final Runnable r) {
            tasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (active == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((active = tasks.poll()) != null) {
                executor.execute(active);
            }
        }
    }
}
