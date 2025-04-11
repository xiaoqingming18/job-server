package com.qingming.jobserver.service.impl;

import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.dao.company.CompanyUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.AccountStatusEnum;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.CompanyInfoVO;
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
        
        // 2. 验证用户名是否存在
        User existingUser = userMapper.selectByUsername(registerDao.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.REGISTER_USER_EXIST, "该用户名已被注册");
        }
        
        // 3. 验证邮箱是否存在
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
        
        // 5. 获取最新的用户ID并为新用户递增ID
        Long latestUserId = userMapper.getLatestUserId();
        Long newUserId = latestUserId + 1;
        
        // 6. 创建企业管理员用户，使用指定的用户ID
        Date now = new Date();
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("id", newUserId);
        userParams.put("username", registerDao.getUsername());
        userParams.put("password", registerDao.getPassword());
        userParams.put("email", registerDao.getEmail());
        userParams.put("role", UserRoleEnum.company_admin.toString());
        userParams.put("accountStatus", AccountStatusEnum.enabled.toString());
        userParams.put("createTime", now);
        userMapper.insertProjectManagerUser(userParams); // 复用现有方法插入用户
        
        // 7. 创建用户对象用于返回
        User user = new User();
        user.setId(newUserId);
        user.setUsername(registerDao.getUsername());
        user.setPassword(registerDao.getPassword());
        user.setEmail(registerDao.getEmail());
        user.setRole(UserRoleEnum.company_admin);
        user.setAccountStatus(AccountStatusEnum.enabled);
        user.setCreateTime(now);
        
        // 8. 更新公司的管理员ID
        company.setAdminId(newUserId);
        companyMapper.updateAdminId(company.getId(), newUserId);
        
        return user;
    }
    
    @Override
    public Company getByLicenseNumber(String licenseNumber) {
        return companyMapper.selectByLicenseNumber(licenseNumber);
    }
    
    @Override
    public CompanyInfoVO getCompanyInfo(Integer companyId) {
        // 参数验证
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        // 查询企业详细信息
        CompanyInfoVO companyInfo = companyMapper.getCompanyInfoById(companyId);
        
        // 判断企业是否存在
        if (companyInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业信息不存在");
        }
        
        return companyInfo;
    }
    
    @Override
    @Transactional
    public CompanyInfoVO updateCompanyInfo(CompanyUpdateDao updateDao, Long userId) {
        // 参数验证
        if (updateDao == null || updateDao.getId() == null || updateDao.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "企业ID不合法");
        }
        
        // 验证企业是否存在
        CompanyInfoVO existingCompany = companyMapper.getCompanyInfoById(updateDao.getId());
        if (existingCompany == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "企业信息不存在");
        }
        
        // 验证用户是否有权限修改企业信息（系统管理员、企业管理员或项目经理）
        if (!isSystemAdmin(userId) && !isCompanyAdmin(userId, updateDao.getId()) && !isProjectManagerOfCompany(userId, updateDao.getId())) {
            throw new BusinessException(ErrorCode.NO_COMPANY_AUTH, "无权修改企业信息");
        }
        
        // 构建更新对象，只更新非null字段
        Company company = new Company();
        company.setId(updateDao.getId());
        company.setName(updateDao.getName());
        company.setAddress(updateDao.getAddress());
        company.setLegalPerson(updateDao.getLegalPerson());
        
        // 执行更新操作
        int rows = companyMapper.updateCompanySelective(company);
        if (rows <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新企业信息失败");
        }
        
        // 返回更新后的企业信息
        return companyMapper.getCompanyInfoById(updateDao.getId());
    }
    
    @Override
    public boolean isProjectManagerOfCompany(Long userId, Integer companyId) {
        if (userId == null || userId <= 0 || companyId == null || companyId <= 0) {
            return false;
        }
        
        // 查询用户是否为企业的项目经理
        Integer count = companyMapper.countProjectManagerByUserIdAndCompanyId(userId, companyId);
        return count != null && count > 0;
    }
    
    @Override
    public boolean isCompanyAdmin(Long userId, Integer companyId) {
        if (userId == null || userId <= 0 || companyId == null || companyId <= 0) {
            return false;
        }
        
        // 查询公司信息
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            return false;
        }
        
        // 检查当前用户是否是该公司的管理员
        return userId.equals(company.getAdminId());
    }
    
    @Override
    public boolean isSystemAdmin(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }
        
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // 检查用户角色是否为系统管理员
        return UserRoleEnum.system_admin.equals(user.getRole());
    }
}
