package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.AccountStatusEnum;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 公司服务实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional
    public User registerCompanyWithManager(CompanyRegisterDao registerDao) {
        // 1. 验证营业执照是否已被注册
        Company existingCompany = companyMapper.selectByLicenseNumber(registerDao.getLicenseNumber());
        if (existingCompany != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该营业执照编号已被注册");
        }
        
        // 2. 验证项目经理用户名是否存在
        User existingUser = userMapper.selectByUsername(registerDao.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.REGISTER_USER_EXIST, "该用户名已被注册");
        }
        
        // 3. 验证项目经理邮箱是否存在
        existingUser = userMapper.selectByEmail(registerDao.getEmail());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已被注册");
        }
        
        // 4. 创建公司
        Company company = new Company();
        company.setName(registerDao.getName());
        company.setLicenseNumber(registerDao.getLicenseNumber());
        company.setAddress(registerDao.getAddress());
        company.setLegalPerson(registerDao.getLegalPerson());
        companyMapper.insertCompany(company);
        
        // 5. 创建项目经理用户
        User user = new User();
        user.setUsername(registerDao.getUsername());
        user.setPassword(registerDao.getPassword());
        user.setEmail(registerDao.getEmail());
        user.setRole(UserRoleEnum.project_manager);
        user.setAccountStatus(AccountStatusEnum.enabled);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        
        // 6. 关联项目经理与公司
        String position = registerDao.getPosition();
        if (position == null || position.trim().isEmpty()) {
            position = "项目经理"; // 如果没有提供职位，使用默认值
        }
        companyMapper.insertProjectManager(user.getId(), company.getId(), position);
        
        return user;
    }
    
    @Override
    public Company getByLicenseNumber(String licenseNumber) {
        return companyMapper.selectByLicenseNumber(licenseNumber);
    }
}
