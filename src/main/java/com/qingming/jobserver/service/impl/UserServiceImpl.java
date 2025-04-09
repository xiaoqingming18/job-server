package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.entity.User;
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
}