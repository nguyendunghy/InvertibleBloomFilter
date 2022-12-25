package com.example.invertiblebloomfilter.ibf;

import com.example.invertiblebloomfilter.utils.Constant;
import com.example.invertiblebloomfilter.utils.PropertyUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Cell {

    private LongLong rowHashSum;
    private long[] keySums;
    private long count;



    /**
     * Construct a new cell from the provided values and hash function. The checkSumHash
     *
     * @param keySums    accumulates the sum of all key elements inserted into this cell
     * @param rowHashSum accumulates the sum of the hash of a row inserted into this cell
     * @param count      the number of elements that have been inserted into this cell; count can be negative when the cell
     *                   contains deleted elements
     */
    public Cell(long[] keySums, LongLong rowHashSum, long count) {
        this.rowHashSum = rowHashSum;
        this.keySums = keySums;
        this.count = count;
    }

    public Cell() {
    }

    /**
     * Replace all values in the cell with the values provided as arguments
     *
     * @param keySums
     * @param rowHashSum
     * @param count
     */
    public void load(long[] keySums, LongLong rowHashSum, long count) {
        this.rowHashSum = rowHashSum;
        this.keySums = keySums;
        this.count = count;
    }

    /**
     * @return returns true when all values in the cell is empty, meaning all values are 0 and the cell contains no
     * inserted or removed elements.
     */
    @JsonIgnore
    public boolean isZero() {
        for (long key : keySums) {
            if (key != 0) {
                return false;
            }
        }
        return count == 0 && rowHashSum.isEmpty();
    }

    /**
     * @return returns true when the cell has a count of 1 or -1, indicate that it *may* represent a single element
     */
    @JsonIgnore
    public boolean isSingular() {
        return count == 1L || count == -1;
    }

    /**
     * Insert an element into the cell
     *
     * @param keySums    long[] representing the element; must have the same array length as the elementSum
     * @param rowHashSum accumulates the sum of the hash of a row inserted into this cell
     */
    public void insert(long[] keySums, LongLong rowHashSum) {
        for (int i = 0; i < keySums.length; i++) {
            this.keySums[i] = aggregate(this.keySums[i], keySums[i], true);
        }
        this.rowHashSum.aggregate(rowHashSum, true);
        count++;
    }

    /**
     * Remove an element from the cell
     *
     * @param keySums long[] representing the element; must have the same array length as the elementSum
     */
    public void remove(long[] keySums, LongLong rowHashSum) {
        for (int i = 0; i < keySums.length; i++) {
            this.keySums[i] = aggregate(this.keySums[i], keySums[i], false);
        }
        this.rowHashSum.aggregate(rowHashSum, false);
        count--;
    }

    /**
     * Add the values in another cell to this cell's values. Equivalent to performing a insert() on this cell for all
     * the elements represented in other cell
     *
     * @param other the other cell
     */
    public void add(Cell other) {
        for (int i = 0; i < other.keySums.length; i++) {
            this.keySums[i] = aggregate(this.keySums[i], other.keySums[i], true);
        }
        this.rowHashSum.aggregate(other.rowHashSum, true);
        count += other.count;
    }

    /**
     * Subtract the values in another cell from this cell's values. Equivalent to performing a remove() on this cell for
     * all the elements represented in other cell
     *
     * @param other the other cell
     */
    public void subtract(Cell other) {
        for (int i = 0; i < other.keySums.length; i++) {
            this.keySums[i] = aggregate(this.keySums[i], other.keySums[i], false);
        }
        this.rowHashSum.aggregate(other.rowHashSum, false);
        this.count -= other.count;
    }


    private long aggregate(long x, long y, boolean insert) {
        if (Constant.IBF_DB_AGG) {
            return insert ? x + y : x - y;
        }

        return x ^ y;
    }


    public long[] keySums() {
        return keySums;
    }

    public LongLong getRowHashSum() {
        return rowHashSum;
    }

    public void setRowHashSum(LongLong rowHashSum) {
        this.rowHashSum = rowHashSum;
    }

//    @JsonIgnore
    public long[] getKeySums() {
        return keySums;
    }

    public void setKeySums(long[] keySums) {
        this.keySums = keySums;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public LongLong rowHashSum() {
        return rowHashSum;
    }

    public Cell copy() {
        return new Cell(Arrays.copyOf(keySums(), keySums().length), rowHashSum().copy(), getCount());
    }

    @Override
    public String toString() {
        return "Cell {rowHashSum=" + rowHashSum + ", keySums=" + Arrays.toString(keySums) + ", count=" + count + "}";
    }

    public static class Serializer extends ByteBufSerializer<Cell> {
        private final int keySumsLength;

        public Serializer(int keySumsLength) {
            this.keySumsLength = keySumsLength;
        }

        @Override
        public Cell decode(ByteBuf byteBuf) {
            long[] keySums = new long[keySumsLength];
            for (int keyIndex = 0; keyIndex < keySumsLength; keyIndex++) {
                keySums[keyIndex] = ByteBufSerializer.long64.decode(byteBuf);
            }
            return new Cell(
                    keySums, new LongLong(new String(ByteBufSerializer.byteArray.decode(byteBuf))), ByteBufSerializer.long64.decode(byteBuf));
        }

        @Override
        public void encode(Cell cell, ByteBuf byteBuf) {
            for (int keyIndex = 0; keyIndex < cell.keySums.length; keyIndex++) {
                ByteBufSerializer.long64.encode(cell.keySums()[keyIndex], byteBuf);
            }
            ByteBufSerializer.byteArray.encode(cell.rowHashSum().getValue().getBytes(StandardCharsets.UTF_8), byteBuf);
            ByteBufSerializer.long64.encode(cell.getCount(), byteBuf);
        }


        //        @Override
        public String getName() {
            return "Cell";
        }
    }
}
