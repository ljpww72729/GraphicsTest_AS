package com.kk.lp.arithmetic;

/**
 * Created by lipeng on 2016 3-11.
 */
public class Fabonacci {

    /**
     * 递归法
     * 时间复杂度：O（2^n）
     * 空间复杂度：数字过大可能导致 栈溢出
     * 数学式的斐波那契数列算法，耗时，会有重复数据计算
     * @param number
     * @return
     */
    public static int computeFabonacciByRecursion(int number){
        if (number <= 2){
            return 1;
        }else{
            return computeFabonacciByRecursion(number - 1) + computeFabonacciByRecursion(number - 2);
        }
    }

    /**
     * 递推法
     * 时间复杂度：O（n）
     * 空间复杂度：O（n）
     * @param number
     * @return
     */
    public int computeFabonacciBy_ditui(int number){
        int[] result = new int[number + 1];
        result[0] = 0;
        result[1] = 1;
        for (int i = 2; i <= number; i++) {
            result[i] = result[i -1] + result[i-2];
        }
        return result[number];
    }

    /**
     * 递推法优化
     * 时间复杂度：O（n）
     * 空间复杂度：O（1）
     * @param number
     * @return
     */
    public int computeFabonacciBy_ditui_youhua(int number){
        int temp = 0;
        int n_1 = 0,n_2 = 1;
        for (int i = 1; i <= number; i++) {
            temp = n_1 + n_2;
            n_2 = n_1;
            n_1 = temp;
        }
        return temp;
    }
}
