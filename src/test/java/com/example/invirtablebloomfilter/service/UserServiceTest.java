package com.example.invirtablebloomfilter.service;


import com.example.invirtablebloomfilter.entity.request.SaveUserRequest;
import com.example.invirtablebloomfilter.repo.UserRepo;
import com.example.invirtablebloomfilter.utils.Constant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;


    @Test
    public void TestSaveUserReturnSuccess(){
        UserService userService = new UserService(userRepo);
        when(userRepo.save(any())).thenReturn(null);
        SaveUserRequest saveUserRequest = SaveUserRequest.builder()
                .name("jack")
                .password("1234562@")
                .phoneNumber("0123456")
                .build();
        Long code = userService.save(saveUserRequest);
        Assertions.assertNull(code);
    }


    @Test
    public void TestSaveUserReturnInvalidPhone(){
        UserService userService = new UserService(userRepo);
        SaveUserRequest saveUserRequest = SaveUserRequest.builder()
                .name("jack")
                .password("1234562@")
                .phoneNumber("ABC123456")
                .build();
        Long code = userService.save(saveUserRequest);
        Assertions.assertEquals(Constant.SAVE_USER_REQUEST_PHONE_INVALID,code);
    }
}
