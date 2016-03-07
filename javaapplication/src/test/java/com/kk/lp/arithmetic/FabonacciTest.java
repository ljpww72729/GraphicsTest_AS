package com.kk.lp.arithmetic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by lipeng on 2016 3-14.
 */
public class FabonacciTest {

    private Fabonacci fabonacci;

    @Test
    public void testComputeFabonacciByRecursion() throws Exception {
        System.out.println(Fabonacci.computeFabonacciByRecursion(40));
    }

    @Test
    public void testEqual() {
        Assert.assertTrue(fabonacci.computeFabonacciBy_ditui(40) == Fabonacci.computeFabonacciByRecursion(40));
    }

    @Test
    public void testComputeFabonacciBy_ditui() throws Exception {
        System.out.println(fabonacci.computeFabonacciBy_ditui(40));
    }

    @Before
    public void setUp() throws Exception {
        fabonacci = new Fabonacci();
    }

    @Test
    public void testComputeFabonacciBy_ditui_youhua() throws Exception {
        System.out.println(fabonacci.computeFabonacciBy_ditui_youhua(40));
    }
}