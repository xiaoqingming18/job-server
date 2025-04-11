package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.user.CompanyAdminLoginDao;
import com.qingming.jobserver.model.dao.user.JobSeekerLoginDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.dao.user.ProjectManagerLoginDao;
import com.qingming.jobserver.model.dao.user.UpdatePasswordDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;
import com.qingming.jobserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User getByUsernameAndPassword(String username, String password) {
        return userMapper.selectByUsernameAndPassword(username, password);
    }

    @Override
    @Transactional
    public User registerJobSeeker(JobSeekerRegisterDao registerDao) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDao.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.REGISTER_USER_EXIST);
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("username", registerDao.getUsername());
        params.put("password", registerDao.getPassword());
        params.put("email", registerDao.getEmail());
        
        userMapper.insertJobSeekerUser(params);
        Long userId = ((Number) params.get("id")).longValue();
        
        Map<String, Object> extParams = new HashMap<>();
        extParams.put("id", userId);
        userMapper.insertJobSeekerExt(extParams);
        
        return userMapper.selectById(userId);
    }

    @Override
    @Transactional
    public JobSeekerProfileVO updateJobSeekerProfile(JobSeekerUpdateInfoDao updateInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", updateInfo.getUserId());
        params.put("avatar", updateInfo.getAvatar());
        params.put("mobile", updateInfo.getMobile());
        params.put("email", updateInfo.getEmail());
        params.put("realName", updateInfo.getRealName());
        params.put("gender", updateInfo.getGender());
        params.put("birthday", updateInfo.getBirthday());
        params.put("expectPosition", updateInfo.getExpectPosition());
        params.put("workYears", updateInfo.getWorkYears());
        params.put("skill", updateInfo.getSkill());
        params.put("certificates", updateInfo.getCertificates());
        params.put("resumeUrl", updateInfo.getResumeUrl());
        
        userMapper.updateJobSeeker(params);
        
        // 返回更新后的完整求职者资料
        return userMapper.getJobSeekerProfile(updateInfo.getUserId());
    }
    
    @Override
    public JobSeekerProfileVO getJobSeekerProfile(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        // 调用Mapper中的方法获取求职者完整资料
        JobSeekerProfileVO profileVO = userMapper.getJobSeekerProfile(userId);
        
        // 判断是否找到对应的求职者资料
        if (profileVO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "求职者资料不存在");
        }
        
        return profileVO;
    }

    @Override
    public boolean updatePassword(Long userId, UpdatePasswordDao updatePasswordDao) {
        // 验证新密码与确认密码是否一致
        if (!updatePasswordDao.getNewPassword().equals(updatePasswordDao.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码与确认密码不一致");
        }
        
        // 验证旧密码是否正确
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        
        if (!user.getPassword().equals(updatePasswordDao.getOldPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "旧密码不正确");
        }
        
        // 更新密码
        int rows = userMapper.updatePassword(userId, updatePasswordDao.getNewPassword());
        return rows > 0;
    }

    @Override
    public User jobSeekerLogin(JobSeekerLoginDao loginDao) {
        // 根据用户名和密码查询用户
        User user = userMapper.selectByUsernameAndPassword(loginDao.getUsername(), loginDao.getPassword());
        
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_PARAMS_ERROR);
        }
        
        // 判断用户角色是否为求职者
        if (user.getRole() != UserRoleEnum.job_seeker) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "非求职者账号，无法登录");
        }
        
        return user;
    }
    
    @Override
    public User companyAdminLogin(CompanyAdminLoginDao loginDao) {
        // 根据用户名和密码查询用户
        User user = userMapper.selectByUsernameAndPassword(loginDao.getUsername(), loginDao.getPassword());
        
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_PARAMS_ERROR);
        }
        
        // 判断用户角色是否为企业管理员
        if (user.getRole() != UserRoleEnum.company_admin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "非企业管理员账号，无法登录");
        }
        
        return user;
    }
    
    @Override
    public User projectManagerLogin(ProjectManagerLoginDao loginDao) {
        // 根据用户名和密码查询用户
        User user = userMapper.selectByUsernameAndPassword(loginDao.getUsername(), loginDao.getPassword());
        
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_PARAMS_ERROR);
        }
        
        // 判断用户角色是否为项目经理
        if (user.getRole() != UserRoleEnum.project_manager) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "非项目经理账号，无法登录");
        }
        
        return user;
    }
}