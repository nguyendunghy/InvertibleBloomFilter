package com.example.invirtablebloomfilter.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveUserRequest {
    private String name;

    private String password;

    private String phoneNumber;
}
