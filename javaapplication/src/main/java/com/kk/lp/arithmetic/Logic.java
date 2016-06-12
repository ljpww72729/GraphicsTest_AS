package com.kk.lp.arithmetic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lipeng on 2016 6-8.
 */
public class Logic {

    /**
     * 获取一个数组中只出现一次并且下标最小的那个元素的下标
     * @param arr
     * @return
     */
    public ArrayEntry getMinIndexOnlyOne(String[] arr) {
        String value = null;
        Map<String,ArrayEntry> map = new HashMap<>();
        LinkedList<ArrayEntry> linkedList = new LinkedList<>();
        for (int i = 0; i < arr.length; i++) {
            String key = arr[i];
            if (map.containsKey(key)){
                map.get(key).incrementCount();
                linkedList.remove(map.get(key));
            }else{
                ArrayEntry ae = new ArrayEntry();
                ae.setIndex(i);
                map.put(key, ae);
                linkedList.add(ae);
            }
        }

        return linkedList.size() > 0 ? linkedList.get(0):null;
    }

    static class ArrayEntry {
        private AtomicInteger count = new AtomicInteger(1);
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCount() {
            return count.get();
        }

        public void incrementCount(){
            count.incrementAndGet();
        }

    }

}
