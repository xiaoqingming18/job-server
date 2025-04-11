package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
import com.qingming.jobserver.model.dao.company.CompanyUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.CompanyInfoVO;

/**
 * 公司服务接口
 */
public interface CompanyService {
    /**
     * 注册公司并创建第一个项目管理员
     * @param registerDao 公司注册信息
     * @return 新创建的项目管理员用户
     */
    User registerCompanyWithManager(CompanyRegisterDao registerDao);
    
    /**
     * 根据营业执照编号查询公司
     * @param licenseNumber 营业执照编号
     * @return 公司实体，若不存在则返回null
     */
    Company getByLicenseNumber(String licenseNumber);
    
    /**
     * 根据企业ID查询企业详细信息
     * @param companyId 企业ID
     * @return 企业详细信息VO对象
     */
    CompanyInfoVO getCompanyInfo(Integer companyId);
    
    /**
     * 更新企业信息
     * @param updateDao 更新的企业信息
     * @param userId 当前用户ID
     * @return 更新后的企业对象
     */
    CompanyInfoVO updateCompanyInfo(CompanyUpdateDao updateDao, Long userId);
    
    /**
     * 检查用户是否为指定企业的项目经理
     * @param userId 用户ID
     * @param companyId 企业ID
     * @return 如果是项目经理返回true，否则返回false
     */
    boolean isProjectManagerOfCompany(Long userId, Integer companyId);
    
    /**
     * 检查用户是否为指定企业的管理员
     * @param userId 用户ID
     * @param companyId 企业ID
     * @return 如果是企业管理员返回true，否则返回false
     */
    boolean isCompanyAdmin(Long userId, Integer companyId);
    
    /**
     * 检查用户是否是系统管理员
     * @param userId 用户ID
     * @return 如果是系统管理员返回true，否则返回false
     */
    boolean isSystemAdmin(Long userId);
}
