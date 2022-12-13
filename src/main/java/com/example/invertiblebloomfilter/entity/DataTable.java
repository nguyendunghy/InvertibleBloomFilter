package com.example.invertiblebloomfilter.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataTable {
    private long rowHashNumber;
    private String stringColumn;
    private Long numberColumn;
    private String  dateColumn;
    private String clobColumn;


}
