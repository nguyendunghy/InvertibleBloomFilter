package com.example.invertiblebloomfilter.entity;

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
    private long rowHash[];
    private long count;
}
