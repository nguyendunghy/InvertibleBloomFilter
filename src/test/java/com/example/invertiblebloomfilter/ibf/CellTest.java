package com.example.invertiblebloomfilter.ibf;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class CellTest {

    @Test
    public void testBuildStringRowHash1(){
        long[] keySum = new long[]{1,2,3,4};
        Cell cell = new Cell();
        cell.setKeySums(keySum);
        String rowHash = cell.buildStringRowHash();
        Assert.assertEquals("1234",rowHash);
    }

    @Test
    public void testBuildStringRowHash2(){
        long[] keySum = new long[]{0,0,0,0};
        Cell cell = new Cell();
        cell.setKeySums(keySum);
        String rowHash = cell.buildStringRowHash();
        Assert.assertEquals("0000",rowHash);
    }


    @Test
    public void testBuildStringRowHash3(){
        long[] keySum = new long[]{10,11,12,13};
        Cell cell = new Cell();
        cell.setKeySums(keySum);
        String rowHash = cell.buildStringRowHash();
        Assert.assertEquals("abcd",rowHash);
    }

    @Test
    public void testBuildStringRowHash4(){
        long[] keySum = new long[]{2664947779l, 128949195l, 3830995238l, 2115696166l};
        Cell cell = new Cell();
        cell.setKeySums(keySum);
        String rowHash = cell.buildStringRowHash();
        Assert.assertEquals("9ED7E0437AF9BCBE45859267E1AF626",rowHash.toUpperCase());
    }

    @Test
    public void testBuildStringRowHash5(){
        long[] keySum = new long[]{1506490200,
                1540541106,
                704927082,
                3140761723l};
        Cell cell = new Cell();
        cell.setKeySums(keySum);
        String rowHash = cell.buildStringRowHash();
        Assert.assertEquals("9ED7E0437AF9BCBE45859267E1AF626",rowHash.toUpperCase());
    }
}
