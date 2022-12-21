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
        if(value == null){
            return 0;
        }
        return (long) value.hashCode();
    }

    public LongLong copy(){
        return new LongLong(this.value);
    }


    @SneakyThrows
    public void xor(LongLong x) {
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
            int tempXor = Integer.parseInt(tempValue[i], 16) ^ Integer.parseInt(tempX[i], 16);
            tempValue[i] = Integer.toHexString(tempXor);
        }

        this.value = String.join("", tempValue).toUpperCase();

    }
}
