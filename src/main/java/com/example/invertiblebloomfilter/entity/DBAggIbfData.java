package com.example.invertiblebloomfilter.entity;

import com.example.invertiblebloomfilter.ibf.LongLong;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DBAggIbfData {
    private int cellIndex;
    private long rowHash;
    private long count;

    public LongLong getRowHash(){
        return new LongLong(rowHash + "");
    }
}
