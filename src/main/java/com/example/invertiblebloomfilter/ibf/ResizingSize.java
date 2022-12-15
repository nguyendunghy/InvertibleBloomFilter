package com.example.invertiblebloomfilter.ibf;

public enum ResizingSize {
    SMALL(1, 1, 1),
    MEDIUM(5, 6, 7),
    LARGE(23, 19, 17),
    XLARGE(5 * 23, 6 * 19, 7 * 17);

    /** Defines the order in which the sizes are attempted when syncing */
    public static final ResizingSize[] SIZES_ATTEMPT_ORDER = new ResizingSize[] {SMALL, MEDIUM, LARGE, XLARGE};

    final int[] resizingFactors;

    ResizingSize(int... resizingFactors) {
        this.resizingFactors = resizingFactors;
    }

    public long[] divisors(int smallCellCount) {
        long[] divisors = new long[InvertibleBloomFilter.K_INDEPENDENT_HASH_FUNCTIONS];
        long[] primeDivisors = OneHashingBloomFilterUtils.primeDivisors(smallCellCount);

        for (int i = 0; i < InvertibleBloomFilter.K_INDEPENDENT_HASH_FUNCTIONS; i++) {
            divisors[i] = resizingFactors[i] * primeDivisors[i];
        }

        return divisors;
    }

    public int cellCountFactor() {
        int min = resizingFactors[0];
        for (int i = 1; i < resizingFactors.length; i++) min = Math.min(min, resizingFactors[i]);
        return min;
    }
}
