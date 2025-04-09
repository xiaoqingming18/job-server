package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.request.LoginRequest;
import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin-login")
    public BaseResponse<User> adminLogin(@RequestBody LoginRequest loginRequest) {
        User user = userService.getByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return ResultUtils.success(user);
        }
        return ResultUtils.<User>error(ErrorCode.LOGIN_PARAMS_ERROR.getCode(), ErrorCode.LOGIN_PARAMS_ERROR.getMessage());
    }

    @PostMapping("/jobseeker-register")
    public BaseResponse<User> jobSeekerRegister(@RequestBody @Valid JobSeekerRegisterDao registerDao) {
        User registeredUser = userService.registerJobSeeker(registerDao);
        return ResultUtils.success(registeredUser);
    }
}
