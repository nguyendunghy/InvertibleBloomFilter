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
public class IbfData {
    private String rowHash;

    public LongLong getRowHash(){
        return new LongLong(rowHash);
    }


}
