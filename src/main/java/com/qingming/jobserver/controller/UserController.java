package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.*;
import com.qingming.jobserver.model.dao.user.AdminLoginDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.vo.TokenVO;
import com.qingming.jobserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import static java.rmi.server.LogStream.log;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/admin-login")
    public BaseResponse<TokenVO> adminLogin(@RequestBody @Valid AdminLoginDao loginRequest) {
        User user = userService.getByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            TokenVO tokenVO = new TokenVO(token);
            return ResultUtils.success(tokenVO);
        }
        return ResultUtils.error(ErrorCode.LOGIN_PARAMS_ERROR.getCode(), ErrorCode.LOGIN_PARAMS_ERROR.getMessage());
    }

    @PostMapping("/jobseeker-register")
    public BaseResponse<TokenVO> jobSeekerRegister(@RequestBody @Valid JobSeekerRegisterDao registerDao) {
        User registeredUser = userService.registerJobSeeker(registerDao);
        String token = jwtUtil.generateToken(registeredUser.getId(), registeredUser.getUsername());
        TokenVO tokenVO = new TokenVO(token);
        return ResultUtils.success(tokenVO);
    }

    @PostMapping("/jobseeker-update-info")
    public BaseResponse<User> updateJobSeekerInfo(@RequestBody @Valid JobSeekerUpdateInfoDao updateInfo) {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        System.out.println("===========================userId:" + currentUserId.toString());
        updateInfo.setUserId(currentUserId);
        User updatedUser = userService.updateJobSeekerProfile(updateInfo);
        return ResultUtils.success(updatedUser);
    }
}