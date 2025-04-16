package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.model.dao.project.ProjectAddDao;
import com.qingming.jobserver.model.dao.project.ProjectStatusUpdateDao;
import com.qingming.jobserver.model.dao.project.ProjectUpdateDao;
import com.qingming.jobserver.model.vo.ProjectInfoVO;
import com.qingming.jobserver.service.ProjectService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    /**
     * 添加建筑项目
     * @param projectAddDao 项目信息
     * @return 添加的项目信息
     */
    @PostMapping("/add")
    public BaseResponse<ProjectInfoVO> addProject(@RequestBody @Valid ProjectAddDao projectAddDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层添加项目
            ProjectInfoVO projectInfo = projectService.addProject(projectAddDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(projectInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("添加项目失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("添加项目发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "添加项目失败，系统异常");
        }
    }
    
    /**
     * 获取项目详情
     * @param id 项目ID
     * @return 项目详细信息
     */
    @GetMapping("/info/{id}")
    public BaseResponse<ProjectInfoVO> getProjectInfo(@PathVariable("id") Integer id) {
        try {
            // 调用服务层获取项目信息
            ProjectInfoVO projectInfo = projectService.getProjectInfo(id);
            
            // 返回成功响应
            return ResultUtils.success(projectInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取项目信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取项目信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取项目信息失败，系统异常");
        }
    }
    
    /**
     * 更新项目信息
     * @param projectUpdateDao 项目更新信息
     * @return 更新后的项目信息
     */
    @PutMapping("/update")
    public BaseResponse<ProjectInfoVO> updateProject(@RequestBody @Valid ProjectUpdateDao projectUpdateDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层更新项目
            ProjectInfoVO projectInfo = projectService.updateProject(projectUpdateDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(projectInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("更新项目信息失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("更新项目信息发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "更新项目信息失败，系统异常");
        }
    }
    
    /**
     * 更新项目状态
     * @param statusUpdateDao 项目状态更新信息
     * @return 更新后的项目信息
     */
    @PutMapping("/status")
    public BaseResponse<ProjectInfoVO> updateProjectStatus(@RequestBody @Valid ProjectStatusUpdateDao statusUpdateDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层更新项目状态
            ProjectInfoVO projectInfo = projectService.updateProjectStatus(statusUpdateDao, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(projectInfo);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("更新项目状态失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("更新项目状态发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "更新项目状态失败，系统异常");
        }
    }
    
    /**
     * 删除项目
     * @param id 项目ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<String> deleteProject(@PathVariable("id") Integer id) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层删除项目
            boolean result = projectService.deleteProject(id, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success("删除成功");
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("删除项目失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("删除项目发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "删除项目失败，系统异常");
        }
    }
    
    /**
     * 获取企业项目列表
     * @param companyId 企业ID
     * @return 项目列表
     */
    @GetMapping("/company/{companyId}/list")
    public BaseResponse<List<ProjectInfoVO>> getCompanyProjectList(@PathVariable("companyId") Integer companyId) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层获取企业项目列表
            List<ProjectInfoVO> projectList = projectService.getCompanyProjectList(companyId, currentUserId);
            
            // 返回成功响应
            return ResultUtils.success(projectList);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取企业项目列表失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取企业项目列表发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取企业项目列表失败，系统异常");
        }
    }
    
    /**
     * 分页获取项目列表
     * @param requestDao 分页请求参数
     * @return 分页项目列表
     */
    @GetMapping("/page")
    public BaseResponse<PageResult<ProjectInfoVO>> getProjectPage(
            @Valid ProjectPageRequestDao requestDao) {
        try {
            // 获取当前登录用户ID
            Long currentUserId = CurrentUserUtils.getCurrentUserId();
            if (currentUserId == null) {
                return ResultUtils.error(ErrorCode.NOT_LOGIN.getCode(), "用户未登录");
            }
            
            // 调用服务层获取分页数据
            PageResult<ProjectInfoVO> pageResult = projectService.getProjectPage(requestDao);
            
            // 返回成功响应
            return ResultUtils.success(pageResult);
        } catch (BusinessException e) {
            // 捕获业务异常并返回错误响应
            log.error("获取项目分页列表失败: {}", e.getMessage());
            return ResultUtils.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常并返回系统错误
            log.error("获取项目分页列表发生系统异常", e);
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR.getCode(), "获取项目分页列表失败，系统异常");
        }
    }
}
