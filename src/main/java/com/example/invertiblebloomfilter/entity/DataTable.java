package com.example.invertiblebloomfilter.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DataTable {
    private String rowHash;
    private String stringColumn;
    private String numberColumn;
    private String  dateColumn;
    private String clobColumn;


}
