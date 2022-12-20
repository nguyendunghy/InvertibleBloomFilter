package com.example.invertiblebloomfilter.ibf;

import java.util.Arrays;

public class IBFDecodeResultElement {
    public final long[] keySum;
    public final LongLong rowHashSum;

    public IBFDecodeResultElement(long[] keySum, LongLong rowHashSum) {
        this.keySum = keySum;
        this.rowHashSum = rowHashSum;
    }

    public long[] getKeySum() {
        return keySum;
    }

    public LongLong getRowHashSum() {
        return rowHashSum;
    }

    @Override
    public String toString() {
        return "{keySum=" + Arrays.toString(keySum) + ", rowHashSum=" + rowHashSum + "}";
    }
}
