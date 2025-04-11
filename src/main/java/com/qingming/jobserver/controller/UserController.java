package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.*;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.user.AdminLoginDao;
import com.qingming.jobserver.model.dao.user.CompanyAdminLoginDao;
import com.qingming.jobserver.model.dao.user.JobSeekerLoginDao;
import com.qingming.jobserver.model.dao.user.ProjectManagerLoginDao;
import com.qingming.jobserver.model.dao.user.UpdatePasswordDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;
import com.qingming.jobserver.model.vo.TokenVO;
import com.qingming.jobserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    
    /**
     * 企业管理员登录
     * @param loginDao 登录请求参数
     * @return 包含token的响应
     */
    @PostMapping("/company-admin-login")
    public BaseResponse<TokenVO> companyAdminLogin(@RequestBody @Valid CompanyAdminLoginDao loginDao) {
        try {
            // 调用Service层进行登录验证
            User user = userService.companyAdminLogin(loginDao);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            TokenVO tokenVO = new TokenVO(token);
            
            // 返回带有token的成功响应
            return ResultUtils.success(tokenVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("企业管理员登录失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("企业管理员登录发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "登录失败，系统异常");
        }
    }
    
    /**
     * 项目经理登录
     * @param loginDao 登录请求参数
     * @return 包含token的响应
     */
    @PostMapping("/project-manager-login")
    public BaseResponse<TokenVO> projectManagerLogin(@RequestBody @Valid ProjectManagerLoginDao loginDao) {
        try {
            // 调用Service层进行登录验证
            User user = userService.projectManagerLogin(loginDao);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            TokenVO tokenVO = new TokenVO(token);
            
            // 返回带有token的成功响应
            return ResultUtils.success(tokenVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("项目经理登录失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("项目经理登录发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "登录失败，系统异常");
        }
    }
    
    /**
     * 求职者登录
     * @param loginDao 登录请求参数
     * @return 包含token的响应
     */
    @PostMapping("/jobseeker-login")
    public BaseResponse<TokenVO> jobSeekerLogin(@RequestBody @Valid JobSeekerLoginDao loginDao) {
        try {
            // 调用Service层进行登录验证
            User user = userService.jobSeekerLogin(loginDao);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            TokenVO tokenVO = new TokenVO(token);
            
            // 返回带有token的成功响应
            return ResultUtils.success(tokenVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("求职者登录失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("求职者登录发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "登录失败，系统异常");
        }
    }

    @PostMapping("/jobseeker-register")
    public BaseResponse<TokenVO> jobSeekerRegister(@RequestBody @Valid JobSeekerRegisterDao registerDao) {
        User registeredUser = userService.registerJobSeeker(registerDao);
        String token = jwtUtil.generateToken(registeredUser.getId(), registeredUser.getUsername());
        TokenVO tokenVO = new TokenVO(token);
        return ResultUtils.success(tokenVO);
    }

    @PostMapping("/jobseeker-update-info")
    public BaseResponse<JobSeekerProfileVO> updateJobSeekerInfo(@RequestBody @Valid JobSeekerUpdateInfoDao updateInfo) {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        System.out.println("===========================userId:" + currentUserId.toString());
        updateInfo.setUserId(currentUserId);
        JobSeekerProfileVO profileVO = userService.updateJobSeekerProfile(updateInfo);
        return ResultUtils.success(profileVO);
    }
    
    /**
     * 获取当前登录求职者的完整资料
     * @return 求职者完整资料
     */
    @GetMapping("/jobseeker-profile")
    public BaseResponse<JobSeekerProfileVO> getJobSeekerProfile() {
        // 从token中获取当前用户ID
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        // 调用Service层方法获取求职者完整资料
        JobSeekerProfileVO profileVO = userService.getJobSeekerProfile(currentUserId);
        // 返回结果
        return ResultUtils.success(profileVO);
    }

    /**
     * 求职者修改密码
     * @param updatePasswordDao 修改密码请求参数
     * @return 修改结果
     */
    @PostMapping("/jobseeker-update-password")
    public BaseResponse<String> updatePassword(@RequestBody @Valid UpdatePasswordDao updatePasswordDao) {
        // 从token中获取当前用户ID
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        
        // 调用Service层修改密码
        boolean result = userService.updatePassword(currentUserId, updatePasswordDao);
        
        // 返回结果
        if (result) {
            return ResultUtils.success("修改成功");
        } else {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "修改失败");
        }
    }
}