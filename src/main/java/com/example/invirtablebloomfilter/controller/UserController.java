package com.example.invirtablebloomfilter.controller;

import com.example.invirtablebloomfilter.entity.request.ListUserRequest;
import com.example.invirtablebloomfilter.entity.request.SaveUserRequest;
import com.example.invirtablebloomfilter.entity.request.UpdateUserRequest;
import com.example.invirtablebloomfilter.entity.response.CommonResponse;
import com.example.invirtablebloomfilter.service.UserService;
import com.example.invirtablebloomfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.invirtablebloomfilter.utils.Constant.MESSAGE_MAP;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public CommonResponse save(@RequestBody SaveUserRequest userRequest) {
        Long code = userService.save(userRequest);
        if (code != null) {
            return CommonResponse.builder()
                    .code(code)
                    .message(MESSAGE_MAP.get(code))
                    .build();
        }

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .build();
    }

    @PutMapping(value = "/", consumes = "application/json", produces = "application/json")
    public CommonResponse update(@RequestBody UpdateUserRequest userRequest) {
        Long code = userService.update(userRequest);
        if (code != null) {
            return CommonResponse.builder()
                    .code(code)
                    .message(MESSAGE_MAP.get(code))
                    .build();
        }

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .build();
    }

    @DeleteMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public CommonResponse delete(@PathVariable("id") Long id) {
        Long code = userService.delete(id);
        if (code != null) {
            return CommonResponse.builder()
                    .code(code)
                    .message(MESSAGE_MAP.get(code))
                    .build();
        }

        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .build();
    }

    @PostMapping(value = "/list", consumes = "application/json", produces = "application/json")
    public CommonResponse list(@RequestBody ListUserRequest listUserRequest) {
        return userService.list(listUserRequest);
    }
}
