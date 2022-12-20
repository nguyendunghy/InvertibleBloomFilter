package com.example.invertiblebloomfilter.ibf;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LongLong {
    private String value;

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }


    public long toLongValue() {
        return (long)super.hashCode();
    }

    @SneakyThrows
    public void xor(LongLong x) {
        if(x == null && this.getValue().length() != x.getValue().length()){
            throw new Exception("invalid input");
        }

        String[] tempValue = value.split("");
        String[] tempX = x.getValue().split("");
        for(int i=0; i< tempValue.length; i++){
            int tempXor = Integer.parseInt(tempValue[i], 16) ^ Integer.parseInt(tempX[i], 16);
            tempValue[i] = Integer.toHexString(tempXor);
        }

        this.value = String.join("", tempValue);

    }
}
