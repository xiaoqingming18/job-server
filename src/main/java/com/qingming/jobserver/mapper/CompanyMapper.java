package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.vo.CompanyInfoVO;
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
    
    /**
     * 根据ID查询企业详细信息
     * @param companyId 企业ID
     * @return 企业详细信息VO
     */
    CompanyInfoVO getCompanyInfoById(@Param("companyId") Integer companyId);
    
    /**
     * 选择性更新企业信息（只更新非null字段）
     * @param company 包含要更新字段的企业对象
     * @return 影响行数
     */
    int updateCompanySelective(Company company);
    
    /**
     * 统计用户是否为指定企业的项目经理
     * @param userId 用户ID
     * @param companyId 企业ID
     * @return 记录数量，大于0表示是项目经理
     */
    Integer countProjectManagerByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Integer companyId);
}
