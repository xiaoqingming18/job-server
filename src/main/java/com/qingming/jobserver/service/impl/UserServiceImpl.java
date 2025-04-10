package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;
import com.qingming.jobserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public User updateJobSeekerProfile(JobSeekerUpdateInfoDao updateInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", updateInfo.getUserId());
        params.put("avatar", updateInfo.getAvatar());
        params.put("mobile", updateInfo.getMobile());
        params.put("email", updateInfo.getEmail());
        params.put("realName", updateInfo.getRealName());
        params.put("gender", updateInfo.getGender());
        
        userMapper.updateJobSeeker(params);
        
        return userMapper.selectById(updateInfo.getUserId());
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
}