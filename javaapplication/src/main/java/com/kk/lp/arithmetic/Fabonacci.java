package com.kk.lp.arithmetic;

/**
 * Created by lipeng on 2016 3-11.
 */
public class Fabonacci {

    /**
     * �ݹ鷨
     * ʱ�临�Ӷȣ�O��2^n��
     * �ռ临�Ӷȣ����ֹ�����ܵ��� ջ���
     * ��ѧʽ��쳲����������㷨����ʱ�������ظ����ݼ���
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
     * ���Ʒ�
     * ʱ�临�Ӷȣ�O��n��
     * �ռ临�Ӷȣ�O��n��
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
     * ���Ʒ��Ż�
     * ʱ�临�Ӷȣ�O��n��
     * �ռ临�Ӷȣ�O��1��
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
