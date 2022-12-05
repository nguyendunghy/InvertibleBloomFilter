package com.example.invirtablebloomfilter.service;

import com.example.invirtablebloomfilter.entity.User;
import com.example.invirtablebloomfilter.entity.request.ListUserRequest;
import com.example.invirtablebloomfilter.entity.request.SaveUserRequest;
import com.example.invirtablebloomfilter.entity.request.UpdateUserRequest;
import com.example.invirtablebloomfilter.entity.response.CommonResponse;
import com.example.invirtablebloomfilter.repo.UserRepo;
import com.example.invirtablebloomfilter.utils.Constant;
import com.example.invirtablebloomfilter.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.invirtablebloomfilter.utils.Constant.MESSAGE_MAP;

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
                .pass(request.getPassword())
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

        User.UserBuilder userBuilder = User.builder();
        if(request.getId() != null){
            userBuilder.id(request.getId());
        }
        if(request.getName() != null){
            userBuilder.name(request.getName());
        }
        if(request.getPassword() != null){
            userBuilder.pass(request.getPassword());
        }
        if(request.getPhoneNumber() != null){
            userBuilder.phoneNumber(request.getPhoneNumber());
        }

        User user = userBuilder.build();
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

        User user = userRepo.findById(request.getId()).orElse(null);
        if(user == null){
            return Constant.USER_NOT_EXIST;
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
       User user = userRepo.findById(userId).orElse(null);
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
