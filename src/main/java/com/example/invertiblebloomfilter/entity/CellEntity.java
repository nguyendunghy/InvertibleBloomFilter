package com.example.invertiblebloomfilter.entity;

import com.example.invertiblebloomfilter.ibf.LongLong;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CellEntity {
    private Long id;
    private Long ibfId;
    private Long cellIndex;
    private LongLong rowHashSum;
    private String keySums;
    private long count;
}
