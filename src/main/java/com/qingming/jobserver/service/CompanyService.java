package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.company.CompanyRegisterDao;
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
}
