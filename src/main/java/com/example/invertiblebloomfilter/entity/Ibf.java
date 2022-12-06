package com.example.invertiblebloomfilter.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ibf {
    private Long id;
    private String divisors;
    private Long keyLengthSum;
}
