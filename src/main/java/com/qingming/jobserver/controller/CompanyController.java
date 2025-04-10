package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.JwtUtil;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.TokenVO;
import com.qingming.jobserver.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 公司管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/company")
public class CompanyController {
    
    private final CompanyService companyService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public CompanyController(CompanyService companyService, JwtUtil jwtUtil) {
        this.companyService = companyService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 注册公司并创建第一个项目经理账号
     * @param registerDao 包含公司信息和项目经理信息的请求对象
     * @return 包含JWT令牌的响应
     */
    @PostMapping("/register")
    public BaseResponse<TokenVO> registerCompany(@RequestBody @Valid CompanyRegisterDao registerDao) {
        try {
            // 调用服务层注册公司和项目经理
            User projectManager = companyService.registerCompanyWithManager(registerDao);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(projectManager.getId(), projectManager.getUsername());
            TokenVO tokenVO = new TokenVO(token);
            
            // 返回成功响应，包含token
            return ResultUtils.success(tokenVO);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("公司注册失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("公司注册发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "注册失败，系统异常");
        }
    }
}
