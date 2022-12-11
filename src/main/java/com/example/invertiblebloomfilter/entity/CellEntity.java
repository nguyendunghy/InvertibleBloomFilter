package com.example.invertiblebloomfilter.entity;

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
    private long rowHashSum;
    private String keySums;
    private long count;
}
