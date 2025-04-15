package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.JwtUtil;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.company.AddProjectManagerDao;
import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.dao.company.CompanyUpdateDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.CompanyInfoVO;
import com.qingming.jobserver.model.vo.TokenVO;
import com.qingming.jobserver.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 注册公司并创建企业管理员账号
     * @param registerDao 包含公司信息和企业管理员信息的请求对象
     * @return 注册结果响应
     */
    @PostMapping("/register")
    public BaseResponse<String> registerCompany(@RequestBody @Valid CompanyRegisterDao registerDao) {
        try {
            // 调用服务层注册公司和企业管理员
            User companyAdmin = companyService.registerCompanyWithManager(registerDao);
            
            // 返回成功响应
            return ResultUtils.success("注册成功");
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
    
    /**
     * 获取企业详细信息
     * @param id 企业ID
     * @return 企业详细信息
     */
    @GetMapping("/info/{id}")
    public BaseResponse<CompanyInfoVO> getCompanyInfo(@PathVariable("id") Integer id) {
        try {
            // 调用服务层获取企业信息
            CompanyInfoVO companyInfo = companyService.getCompanyInfo(id);
            
            // 返回成功响应
            return ResultUtils.success(companyInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取企业信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取企业信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取企业信息失败，系统异常");
        }
    }
    
    /**
     * 更新企业信息
     * @param updateDao 更新的企业信息
     * @return 更新后的企业信息
     */
    @PutMapping("/update")
    public BaseResponse<CompanyInfoVO> updateCompanyInfo(@RequestBody @Valid CompanyUpdateDao updateDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层更新企业信息
            CompanyInfoVO companyInfo = companyService.updateCompanyInfo(updateDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(companyInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("更新企业信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("更新企业信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "更新企业信息失败，系统异常");
        }
    }
    
    /**
     * 添加项目经理
     * @param addProjectManagerDao 添加项目经理请求参数
     * @return 添加结果响应
     */
    @PostMapping("/add-project-manager")
    public BaseResponse<String> addProjectManager(@RequestBody @Valid AddProjectManagerDao addProjectManagerDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层添加项目经理
            boolean result = companyService.addProjectManager(addProjectManagerDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success("添加成功");
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("添加项目经理失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("添加项目经理发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "添加项目经理失败，系统异常");
        }
    }
    
    /**
     * 删除企业
     * @param id 企业ID
     * @return 删除结果响应
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<String> deleteCompany(@PathVariable("id") Integer id) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层删除企业
            boolean result = companyService.deleteCompany(id, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success("删除成功");
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("删除企业失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("删除企业发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "删除企业失败，系统异常");
        }
    }
    
    /**
     * 强制删除企业（先删除企业下所有项目，然后删除企业）
     * @param id 企业ID
     * @return 删除结果响应
     */
    @DeleteMapping("/force-delete/{id}")
    public BaseResponse<String> forceDeleteCompany(@PathVariable("id") Integer id) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层强制删除企业
            boolean result = companyService.forceDeleteCompany(id, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success("强制删除成功");
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("强制删除企业失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("强制删除企业发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "强制删除企业失败，系统异常");
        }
    }
}
