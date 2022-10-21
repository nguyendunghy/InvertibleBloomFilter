package com.example.statrystesting.service;

import com.example.statrystesting.entity.User;
import com.example.statrystesting.entity.request.ListUserRequest;
import com.example.statrystesting.entity.request.SaveUserRequest;
import com.example.statrystesting.entity.request.UpdateUserRequest;
import com.example.statrystesting.entity.response.CommonResponse;
import com.example.statrystesting.repo.UserRepo;
import com.example.statrystesting.utils.Constant;
import com.example.statrystesting.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.statrystesting.utils.Constant.MESSAGE_MAP;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepo userRepo;


    public CommonResponse list(ListUserRequest request) {
        Long code = validListUserRequest(request);
        if (code != null) {
            return CommonResponse.builder()
                    .code(code)
                    .message(MESSAGE_MAP.get(code))
                    .build();
        }

        List<User> listUser = userRepo.findAll();
        return CommonResponse.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESS)
                .value(listUser)
                .build();
    }

    public Long validListUserRequest(ListUserRequest request) {
        return null;
    }

    public Long save(SaveUserRequest request) {
        Long code = validSaveUserRequest(request);
        if (code != null) {
            return code;
        }

        User user = User.builder()
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepo.save(user);
        return null;
    }

    public Long update(UpdateUserRequest request) {
        Long code = validateUpdateRequest(request);
        if (code != null) {
            return code;
        }

        User user = User.builder()
                .id(request.getId())
                .name(request.getName())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepo.save(user);
        return null;
    }

    private Long validateUpdateRequest(UpdateUserRequest request) {
        if(request.getId() == null){
            return Constant.USER_ID_REQUIRED;
        }

        if (StringUtils.isNullOrEmpty(request.getName())) {
            return Constant.SAVE_USER_REQUEST_NAME_NULL;
        }

        if (StringUtils.isNullOrEmpty(request.getPassword())) {
            return Constant.SAVE_USER_REQUEST_PASSWORD_NULL;
        }

        if (StringUtils.notNullOrEmpty(request.getPhoneNumber()) && !StringUtils.isNumeric(request.getPhoneNumber())) {
            return Constant.SAVE_USER_REQUEST_PHONE_INVALID;
        }

        return null;
    }

    public Long validSaveUserRequest(SaveUserRequest request) {
        if (request == null) {
            return Constant.SAVE_USER_REQUEST_NULL;
        }

        if (StringUtils.isNullOrEmpty(request.getName())) {
            return Constant.SAVE_USER_REQUEST_NAME_NULL;
        }

        if (StringUtils.isNullOrEmpty(request.getPassword())) {
            return Constant.SAVE_USER_REQUEST_PASSWORD_NULL;
        }

        if (StringUtils.notNullOrEmpty(request.getPhoneNumber()) && !StringUtils.isNumeric(request.getPhoneNumber())) {
            return Constant.SAVE_USER_REQUEST_PHONE_INVALID;
        }

        return null;

    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Long delete(Long userId){
       User user = userRepo.getReferenceById(userId);
       if(user == null){
           return Constant.USER_NOT_EXIST;
       }
        userRepo.delete(user);
       return null;
    }

    public User update(User user){
        return userRepo.save(user);
    }
}
