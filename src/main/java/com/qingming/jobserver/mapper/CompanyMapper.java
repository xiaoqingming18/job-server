package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.Company;
import org.apache.ibatis.annotations.Param;

/**
 * 企业信息Mapper接口
 */
public interface CompanyMapper extends BaseMapper<Company> {
    /**
     * 根据营业执照编号查询企业
     * @param licenseNumber 营业执照编号
     * @return 企业实体
     */
    Company selectByLicenseNumber(String licenseNumber);
    
    /**
     * 插入企业信息
     * @param company 企业实体对象
     * @return 影响行数
     */
    int insertCompany(Company company);
    
    /**
     * 插入项目经理信息
     * @param userId 用户ID
     * @param companyId 公司ID
     * @param position 职位名称
     * @return 影响行数
     */
    int insertProjectManager(@Param("userId") Long userId, @Param("companyId") Integer companyId, @Param("position") String position);
}
