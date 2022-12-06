package com.example.invertiblebloomfilter.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IbfEntity {
    private Long id;
    private String divisors;
    private Long keyLengthSum;
}
