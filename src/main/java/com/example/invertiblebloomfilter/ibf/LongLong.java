package com.example.invertiblebloomfilter.ibf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LongLong {
    private String value;

    @JsonIgnore
    private boolean ibfDbAgg = true;
    @JsonIgnore
    private static int radix = 10;

    public LongLong(String value) {
        if (value == null || value.isEmpty()) {
            value = "0";
        }
        this.value = value;
        this.trimLeftZero();
    }

    public boolean isIbfDbAgg() {
        return ibfDbAgg;
    }

    @JsonIgnore
    public boolean isEmpty() {
        if (value == null || value.isEmpty()) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '0') {
                return false;
            }
        }

        return true;
    }


    public long longValue() {
        if (value == null) {
            return 0;
        }
        return (long) value.hashCode();
    }

    public long mod(long divisor){
        if(isEmpty()){
            return 0;
        }
        String value = this.value;
        int signed = 1;
        if(isNegativeNumber()){
            value = this.changeSign().getValue();
            signed = -1;
        }

        long temp = 0;
        for(int i=0, j=1; i< value.length() && j<= value.length(); ){
            temp = Long.parseLong(temp + value.substring(i,j),radix);
            if(temp < divisor){
                j++;
                if(j > value.length()){
                    return temp * signed;
                }
                temp = 0;
            }else{
                temp = temp % divisor;
                i = j;
                j= i+1;
            }
        }

        return temp * signed;
    }

    public LongLong copy() {
        return new LongLong(this.value);
    }

    public static LongLong sum(LongLong a, LongLong b) {
        LongLong x = a.copy();
        LongLong y = b.copy();

        if (y == null || y.isEmpty()) {
            return x;
        }

        if (x == null || x.isEmpty()) {
            return y;
        }

        if(x.isNegativeNumber()){
            if(y.isNegativeNumber()){
                LongLong temp = sum2Positive(x.changeSign(),y.changeSign());
                return temp.changeSign();
            }else{
                return subtract2Positive(y, x.changeSign());
            }
        }else{
            if(y.isNegativeNumber()){
                return subtract2Positive(x,y.changeSign());
            }else{
                return sum2Positive(x,y);
            }
        }
    }

    private static LongLong sum2Positive(LongLong a, LongLong b) {
        LongLong x = a.copy();
        LongLong y = b.copy();

        if (y == null || y.isEmpty()) {
            return x;
        }

        if (x == null || x.isEmpty()) {
            return y;
        }

        int maxLength = Integer.max(x.value.length(), y.value.length());

        String[] tempValue = lpadZero(x.value, maxLength).split("");
        String[] tempX = lpadZero(y.value, maxLength).split("");

        int memory = 0;
        for (int i = tempValue.length - 1; i >= 0; i--) {
            int tempSum = Integer.parseInt(tempValue[i], radix) + Integer.parseInt(tempX[i], radix) + memory;
            memory = tempSum / radix;
            tempValue[i] = Integer.toHexString(tempSum % radix);
        }

        x.value = String.join("", tempValue).toUpperCase();
        if (memory != 0) {
            x.value = Integer.toHexString(memory % radix).toUpperCase() + x.value;
        }
        return x;
    }

    private static String lpadZero(String x, int targetLen) {
        if (x == null) {
            x = "";
        }

        if (x.length() >= targetLen) {
            return x;
        }

        for (int i = x.length(); i < targetLen; i++) {
            x = "0" + x;
        }
        return x;
    }

    public static LongLong subtract(LongLong a, LongLong b) {
        LongLong x = a.copy();
        LongLong y = b.copy();

        if (y == null || y.isEmpty()) {
            return x;
        }

        if (x == null || x.isEmpty()) {
            return y;
        }

        if (x.isNegativeNumber()) {
            if (y.isNegativeNumber()) {
                return subtract2Positive(y.changeSign(), x.changeSign());
            } else {
                LongLong temp = sum2Positive(x.changeSign(), y);
                return temp.changeSign();
            }
        } else {
            if (y.isNegativeNumber()) {
                return sum2Positive(x, y.changeSign());
            } else {
                return subtract2Positive(x, y);
            }
        }
    }

    private static LongLong subtract2Positive(LongLong a, LongLong b) {
        LongLong x = a.copy();
        LongLong y = b.copy();

        boolean compare = compare2Positive(x, y);

        LongLong bigger = compare ? x : y;
        LongLong smaller = compare ? y : x;

        bigger.trimLeftZero();
        smaller.trimLeftZero();

        String[] biggerArr = bigger.getValue().split("");
        String[] smallerArr = smaller.getValue().split("");

        int memory = 0;
        for (int i = biggerArr.length - 1, j = smallerArr.length - 1; i >= 0; i--, j--) {
            int subtrahend = Integer.parseInt(biggerArr[i], radix);

            int subtractValue = memory;
            if (j >= 0) {
                subtractValue = Integer.parseInt(smallerArr[j], radix) + memory;
            }

            if (subtrahend < subtractValue) {
                subtrahend = subtrahend + radix;
                memory = 1;
            } else {
                memory = 0;
            }

            int tempSubtract = subtrahend - subtractValue;
            biggerArr[i] = Integer.toHexString(tempSubtract);
        }


        LongLong tempResult = new LongLong(String.join("", biggerArr).toUpperCase());

        if (!compare) {
            return tempResult.changeSign();
        }
        return tempResult;
    }

    private static boolean compare2Positive(LongLong a, LongLong b) {
        LongLong x = a.copy();
        LongLong y = b.copy();

        x.trimLeftZero();
        y.trimLeftZero();


        if (x.getValue().length() == y.getValue().length()) {
            for (int i = 0; i < x.getValue().length() - 1; i++) {
                if (x.getValue().charAt(i) != y.getValue().charAt(i)) {
                    return Integer.parseInt(x.getValue().substring(i, i + 1), radix) >=
                            Integer.parseInt(y.getValue().substring(i, i + 1), radix);
                }
            }

            return true;

        }

        return x.getValue().length() > y.getValue().length();
    }


    private LongLong trimLeftZero() {
        if (this.value == null || this.value.isEmpty()) {
            return this;
        }
        int start = 0;
        for (int i = 0; i < this.value.length(); i++) {
            if (this.value.charAt(i) != '0') {
                break;
            }
            start++;
        }

        String temp = this.value.substring(start);
        if (temp.isEmpty()) {
            temp = "0";
        }
        this.value = temp;
        return this;
    }


    private boolean isNegativeNumber() {
        return !isEmpty() && this.value.startsWith("-");
    }

    private LongLong changeSign() {
        if (isEmpty()) {
            return this;
        }
        if (this.value.startsWith("-")) {
            this.value = this.value.substring(1);
            return this;
        }

        this.value = "-" + this.value;
        return this;
    }


    public void aggregate(LongLong x, boolean insert) {
        if (!ibfDbAgg) {
            xor(x);
            return;
        }

        if (insert) {
            LongLong temp = sum(this, x);
            this.value = temp.getValue();
        } else {
            LongLong temp = subtract(this, x);
            this.value = temp.getValue();
        }
    }


    @SneakyThrows
    private void xor(LongLong x) {
        if (x == null || x.isEmpty()) {
            return;
        }

        if (this.isEmpty()) {
            this.value = x.value;
            return;
        }

        if (this.value.length() != x.getValue().length()) {
            throw new Exception("Invalid input length");
        }

        String[] tempValue = this.value.split("");
        String[] tempX = x.getValue().split("");
        for (int i = 0; i < tempValue.length; i++) {
            int tempXor = Integer.parseInt(tempValue[i], radix) ^ Integer.parseInt(tempX[i], radix);
            tempValue[i] = Integer.toHexString(tempXor);
        }

        this.value = String.join("", tempValue).toUpperCase();

    }
}
