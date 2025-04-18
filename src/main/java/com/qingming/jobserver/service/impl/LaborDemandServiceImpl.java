package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingming.jobserver.common.ErrorCode;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.LaborDemandMapper;
import com.qingming.jobserver.mapper.ProjectMapper;
import com.qingming.jobserver.mapper.UserMapper;
import com.qingming.jobserver.mapper.CompanyMapper;
import com.qingming.jobserver.mapper.OccupationMapper;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.entity.Company;
import com.qingming.jobserver.model.entity.ConstructionProject;
import com.qingming.jobserver.model.entity.LaborDemand;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.enums.UserRoleEnum;
import com.qingming.jobserver.model.vo.LaborDemandVO;
import com.qingming.jobserver.service.LaborDemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 劳务需求服务实现类
 */
@Slf4j
@Service
public class LaborDemandServiceImpl extends ServiceImpl<LaborDemandMapper, LaborDemand> implements LaborDemandService {    private final LaborDemandMapper laborDemandMapper;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final OccupationMapper occupationMapper;
    
    public LaborDemandServiceImpl(LaborDemandMapper laborDemandMapper, ProjectMapper projectMapper, UserMapper userMapper, CompanyMapper companyMapper, OccupationMapper occupationMapper) {
        this.laborDemandMapper = laborDemandMapper;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.occupationMapper = occupationMapper;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO addLaborDemand(LaborDemandAddDao laborDemandAddDao, Long userId) {
        // 1. 检查项目是否存在
        ConstructionProject project = projectMapper.selectById(laborDemandAddDao.getProjectId());
        if (project == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "项目不存在");
        }
        
        // 2. 检查用户是否有权限操作该项目
        if (!hasProjectPermission(project, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限操作此项目");
        }
        
        // 3. 检查日期参数逻辑
        Date startDate = laborDemandAddDao.getStartDate();
        Date endDate = laborDemandAddDao.getEndDate();
        if (startDate.after(endDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "开始日期不能晚于结束日期");
        }
        
        // 4. 构建劳务需求实体并保存
        LaborDemand laborDemand = new LaborDemand();
        BeanUtils.copyProperties(laborDemandAddDao, laborDemand);
        laborDemand.setStatus("open"); // 设置初始状态为开放中
        laborDemand.setCreateTime(new Date());
        
        // 5. 插入数据
        this.save(laborDemand);
        
        // 6. 查询并返回完整的劳务需求信息
        return this.getLaborDemandInfo(laborDemand.getId());
    }
    
    @Override
    public LaborDemandVO getLaborDemandInfo(Integer demandId) {
        // 查询劳务需求详情
        LaborDemandVO laborDemandVO = laborDemandMapper.getLaborDemandById(demandId);
        if (laborDemandVO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        return laborDemandVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO updateLaborDemand(LaborDemandUpdateDao laborDemandUpdateDao, Long userId) {
        // 1. 检查劳务需求是否存在
        Integer demandId = laborDemandUpdateDao.getId();
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限修改此劳务需求");
        }
        
        // 3. 如果更新了开始和结束日期，检查日期逻辑
        Date startDate = laborDemandUpdateDao.getStartDate();
        Date endDate = laborDemandUpdateDao.getEndDate();
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "开始日期不能晚于结束日期");
        }
        
        // 4. 更新内容
        LaborDemand updateDemand = new LaborDemand();
        BeanUtils.copyProperties(laborDemandUpdateDao, updateDemand);
        updateDemand.setUpdateTime(new Date());
        
        // 5. 更新数据
        this.updateById(updateDemand);
        
        // 6. 查询并返回更新后的完整劳务需求信息
        return this.getLaborDemandInfo(demandId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaborDemandVO updateLaborDemandStatus(LaborDemandStatusUpdateDao statusUpdateDao, Long userId) {
        // 1. 检查劳务需求是否存在
        Integer demandId = statusUpdateDao.getId();
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限修改此劳务需求状态");
        }
        
        // 3. 检查状态参数是否有效
        String status = statusUpdateDao.getStatus();
        if (!("open".equals(status) || "filled".equals(status) || "cancelled".equals(status) || "expired".equals(status))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "无效的状态值");
        }
        
        // 4. 更新状态
        LambdaUpdateWrapper<LaborDemand> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LaborDemand::getId, demandId)
                .set(LaborDemand::getStatus, status)
                .set(LaborDemand::getUpdateTime, new Date());
        
        this.update(updateWrapper);
        
        // 5. 查询并返回更新后的完整劳务需求信息
        return this.getLaborDemandInfo(demandId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteLaborDemand(Integer demandId, Long userId) {
        // 1. 检查劳务需求是否存在
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND.getCode(), "劳务需求不存在");
        }
        
        // 2. 检查权限
        if (!checkOperationPermission(demandId, userId)) {
            throw new BusinessException(ErrorCode.ROLE_NO_PERMISSION.getCode(), "您没有权限删除此劳务需求");
        }
        
        // 3. 执行删除操作
        return this.removeById(demandId);
    }
    
    @Override
    public Page<LaborDemandVO> queryLaborDemandList(LaborDemandQueryDao queryDao) {
        // 创建分页对象
        Page<LaborDemandVO> page = new Page<>(queryDao.getPageNum(), queryDao.getPageSize());
        
        // 调用Mapper查询
        return laborDemandMapper.queryLaborDemandList(
                page,
                queryDao.getProjectId(),
                queryDao.getCompanyId(),
                queryDao.getJobTypeId(),
                queryDao.getMinDailyWage(),
                queryDao.getMaxDailyWage(),
                queryDao.getStartDate(),
                queryDao.getEndDate(),
                queryDao.getAccommodation(),
                queryDao.getMeals(),
                queryDao.getStatus()
        );
    }
    
    @Override
    public List<LaborDemandVO> getLaborDemandsByProjectId(Integer projectId) {
        // 检查项目是否存在
        ConstructionProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "项目不存在");
        }
        
        // 查询并返回指定项目的所有劳务需求
        return laborDemandMapper.getLaborDemandsByProjectId(projectId);
    }
    
    @Override
    public Boolean checkOperationPermission(Integer demandId, Long userId) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;        }
        
        // 系统管理员拥有所有权限
        if (UserRoleEnum.system_admin.equals(user.getRole())) {
            return true;
        }
        
        // 获取需求信息
        LaborDemand laborDemand = this.getById(demandId);
        if (laborDemand == null) {
            return false;
        }
        
        // 获取项目信息
        ConstructionProject project = projectMapper.selectById(laborDemand.getProjectId());
        if (project == null) {
            return false;
        }
        
        // 项目经理有权限
        if (UserRoleEnum.project_manager.equals(user.getRole()) && userId.equals(project.getProjectManagerId())) {
            return true;
        }
        
        // 企业管理员有权限
        if (UserRoleEnum.company_admin.equals(user.getRole())) {
            // 检查用户是否为该项目所属企业的管理员
            Company company = companyMapper.selectById(project.getCompanyId());
            if (company != null && userId.equals(company.getAdminId())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检查用户是否有权限操作项目
     * @param project 项目信息
     * @param userId 用户ID
     * @return 是否有权限
     */
    private boolean hasProjectPermission(ConstructionProject project, Long userId) {
        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;        }
        
        // A. 系统管理员拥有所有权限
        if (UserRoleEnum.system_admin.equals(user.getRole())) {
            return true;
        }
        
        // B. 项目经理有权限
        if (UserRoleEnum.project_manager.equals(user.getRole()) && userId.equals(project.getProjectManagerId())) {
            return true;
        }
        
        // C. 企业管理员有权限
        if (UserRoleEnum.company_admin.equals(user.getRole())) {
            // 检查用户是否为该项目所属企业的管理员
            Company company = companyMapper.selectById(project.getCompanyId());
            if (company != null && userId.equals(company.getAdminId())) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public Page<LaborDemandVO> searchLaborDemands(String keyword, String province, String city, 
                                          String district, Double minDailyWage, Double maxDailyWage, 
                                          String startDateFrom, String startDateTo, 
                                          Integer page, Integer size) {
        // 创建分页对象
        Page<LaborDemandVO> pageObj = new Page<>(page, size);
        
        // 转换日期格式（如果有）
        Date startFrom = null;
        Date startTo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            if (StringUtils.hasText(startDateFrom)) {
                startFrom = sdf.parse(startDateFrom);
            }
            if (StringUtils.hasText(startDateTo)) {
                startTo = sdf.parse(startDateTo);
            }
        } catch (ParseException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "日期格式错误，请使用yyyy-MM-dd格式");
        }
        
        BigDecimal minWage = null;
        BigDecimal maxWage = null;
        if (minDailyWage != null) {
            minWage = BigDecimal.valueOf(minDailyWage);
        }
        if (maxDailyWage != null) {
            maxWage = BigDecimal.valueOf(maxDailyWage);
        }
        
        // 调用Mapper层进行搜索查询
        return laborDemandMapper.searchLaborDemands(
                pageObj,
                keyword,
                province,
                city,
                district,
                minWage,
                maxWage,
                startFrom,
                startTo
        );
    }
    
    @Override
    public List<LaborDemandVO> getRecommendedLaborDemands(Integer limit) {
        // 根据一定的推荐逻辑获取热门/推荐劳务需求
        // 这里采用的简单策略：获取状态为open，且日薪较高的几个需求
        return laborDemandMapper.getRecommendedLaborDemands(limit);
    }
    
    @Override
    public Map<String, Object> getProjectDemandStats(Integer projectId) {
        // 检查项目是否存在
        ConstructionProject project = projectMapper.selectById(projectId);
        if (project == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "项目不存在");
        }
        
        // 获取项目需求统计信息
        Map<String, Object> statsData = new HashMap<>();
        
        // 1. 查询总需求数
        LambdaQueryWrapper<LaborDemand> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LaborDemand::getProjectId, projectId);
        long totalDemandCount = this.count(queryWrapper);
        statsData.put("totalDemandCount", totalDemandCount);
        
        // 2. 查询总人数
        Integer totalHeadcount = laborDemandMapper.getTotalHeadcountByProject(projectId);
        statsData.put("totalHeadcount", totalHeadcount == null ? 0 : totalHeadcount);
        
        // 3. 查询开放中需求数
        LambdaQueryWrapper<LaborDemand> openWrapper = new LambdaQueryWrapper<>();
        openWrapper.eq(LaborDemand::getProjectId, projectId)
                  .eq(LaborDemand::getStatus, "open");
        long openDemandCount = this.count(openWrapper);
        statsData.put("openDemandCount", openDemandCount);
        
        // 4. 查询已满需求数
        LambdaQueryWrapper<LaborDemand> filledWrapper = new LambdaQueryWrapper<>();
        filledWrapper.eq(LaborDemand::getProjectId, projectId)
                    .eq(LaborDemand::getStatus, "filled");
        long filledDemandCount = this.count(filledWrapper);
        statsData.put("filledDemandCount", filledDemandCount);
        
        // 5. 计算总成本
        BigDecimal totalCost = laborDemandMapper.getTotalCostByProject(projectId);
        statsData.put("totalCost", totalCost == null ? BigDecimal.ZERO : totalCost);
        
        // 6. 获取工种分布
        List<Map<String, Object>> occupationDistribution = laborDemandMapper.getOccupationDistributionByProject(projectId);
        statsData.put("occupationDistribution", occupationDistribution);
        
        return statsData;
    }
    
    @Override
    public Map<String, Object> getCompanyDemandStats(Integer companyId) {
        // 检查公司是否存在
        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "公司不存在");
        }
        
        // 获取公司需求统计信息
        Map<String, Object> statsData = new HashMap<>();
        
        // 1. 获取公司下所有项目
        List<ConstructionProject> projects = projectMapper.getProjectsByCompany(companyId);
        List<Integer> projectIds = projects.stream().map(ConstructionProject::getId).collect(Collectors.toList());
        
        if (projectIds.isEmpty()) {
            // 如果没有项目，返回空统计
            statsData.put("totalProjects", 0);
            statsData.put("totalDemands", 0);
            statsData.put("totalHeadcount", 0);
            statsData.put("totalCost", BigDecimal.ZERO);
            statsData.put("projectDistribution", new ArrayList<>());
            return statsData;
        }
        
        // 2. 统计基本信息
        statsData.put("totalProjects", projects.size());
        
        // 3. 查询总需求数
        Integer totalDemands = laborDemandMapper.getTotalDemandsByCompany(companyId);
        statsData.put("totalDemands", totalDemands == null ? 0 : totalDemands);
        
        // 4. 总人数
        Integer totalHeadcount = laborDemandMapper.getTotalHeadcountByCompany(companyId);
        statsData.put("totalHeadcount", totalHeadcount == null ? 0 : totalHeadcount);
        
        // 5. 总成本
        BigDecimal totalCost = laborDemandMapper.getTotalCostByCompany(companyId);
        statsData.put("totalCost", totalCost == null ? BigDecimal.ZERO : totalCost);
        
        // 6. 项目分布
        List<Map<String, Object>> projectDistribution = laborDemandMapper.getProjectDistributionByCompany(companyId);
        statsData.put("projectDistribution", projectDistribution);
        
        return statsData;
    }
    
    @Override
    public List<Map<String, Object>> analyzeOccupationHeat(Integer period) {
        // 计算开始日期（当前日期往前推period天）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -period);
        Date startDate = calendar.getTime();
        
        // 获取统计期内各工种的需求热度数据
        return laborDemandMapper.getOccupationHeatStats(startDate);
    }
    
    @Override
    public Page<LaborDemandVO> getLaborDemandsByOccupation(Integer occupationId, String status, Integer page, Integer size) {
        // 检查工种是否存在
        Occupation occupation = occupationMapper.selectById(occupationId);
        if (occupation == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR.getCode(), "工种不存在");
        }
        
        // 创建分页对象
        Page<LaborDemandVO> pageObj = new Page<>(page, size);
        
        // 调用Mapper查询
        return laborDemandMapper.getLaborDemandsByOccupation(pageObj, occupationId, status);
    }
    
    @Override
    public List<Map<String, Object>> suggestOccupations(Map<String, Object> projectFeatures, String demandDescription) {
        // 这是一个基于规则的简单实现
        // 实际项目中可能需要更复杂的匹配算法或AI推荐
        
        // 获取所有工种信息
        List<Occupation> allOccupations = occupationMapper.selectList(null);
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 基于项目特征和需求描述计算匹配度
        for (Occupation occupation : allOccupations) {
            Map<String, Object> occItem = new HashMap<>();
            occItem.put("occupationId", occupation.getId());
            occItem.put("occupationName", occupation.getName());
            
            // 计算匹配度（简单实现）
            double matchScore = calculateMatchScore(occupation, projectFeatures, demandDescription);
            occItem.put("matchScore", matchScore);
            
            occItem.put("averageWage", occupation.getAverageDailyWage());
            occItem.put("description", occupation.getDescription());
            occItem.put("requiredCertificates", occupation.getRequiredCertificates());
            
            result.add(occItem);
        }
        
        // 按匹配度排序
        result.sort((a, b) -> {
            Double scoreA = (Double) a.get("matchScore");
            Double scoreB = (Double) b.get("matchScore");
            return Double.compare(scoreB, scoreA); // 降序排列
        });
        
        // 只返回前10个匹配度较高的
        return result.size() > 10 ? result.subList(0, 10) : result;
    }
    
    /**
     * 计算工种与需求的匹配度
     * @param occupation 工种信息
     * @param projectFeatures 项目特征
     * @param demandDescription 需求描述
     * @return 匹配度（0-1之间的值）
     */
    private double calculateMatchScore(Occupation occupation, Map<String, Object> projectFeatures, String demandDescription) {
        // 这里是一个简化的匹配计算逻辑，实际系统可能需要更复杂的算法
        double score = 0.6; // 基础分
        
        // 1. 如果需求描述中包含工种名称，增加匹配度
        if (StringUtils.hasText(demandDescription) && 
            StringUtils.hasText(occupation.getName()) && 
            demandDescription.contains(occupation.getName())) {
            score += 0.3;
        }
        
        // 2. 如果需求描述中包含工种描述中的关键词，增加匹配度
        if (StringUtils.hasText(demandDescription) && 
            StringUtils.hasText(occupation.getDescription())) {
            String[] descKeywords = occupation.getDescription().split("，|,|。|、|\\s+");
            for (String keyword : descKeywords) {
                if (keyword.length() > 1 && demandDescription.contains(keyword)) {
                    score += 0.1;
                    break;
                }
            }
        }
        
        // 确保分数在0-1之间
        return Math.min(1.0, Math.max(0.0, score));
    }
    
    @Override
    public Page<LaborDemandVO> getLaborDemandsByLocation(String province, String city, String district, Integer page, Integer size) {
        // 创建分页对象
        Page<LaborDemandVO> pageObj = new Page<>(page, size);
        
        // 调用Mapper查询
        return laborDemandMapper.getLaborDemandsByLocation(pageObj, province, city, district);
    }
    
    @Override
    public List<Map<String, Object>> getHotDemandLocations(Integer limit) {
        // A. 按地区统计需求数量
        List<Map<String, Object>> hotLocations = laborDemandMapper.getHotDemandLocations(limit);
        
        // B. 对于每个热门地区，额外获取项目数和平均工资
        for (Map<String, Object> location : hotLocations) {
            String province = (String) location.get("province");
            String city = (String) location.get("city");
            
            // 获取项目数
            Integer projectCount = projectMapper.getProjectCountByLocation(province, city);
            location.put("projectCount", projectCount);
            
            // 获取平均日薪
            BigDecimal avgWage = laborDemandMapper.getAverageWageByLocation(province, city);
            location.put("averageWage", avgWage);
        }
        
        return hotLocations;
    }
    
    @Override
    public Page<LaborDemandVO> queryLaborDemandsByCriteria(Integer projectType, Integer occupationId, String location, Integer page, Integer size) {
        // 创建分页对象 - 注意这里需要使用与实体类匹配的分页对象
        Page<LaborDemand> pageObj = new Page<>(page, size);
        
        // 解析地区参数（可能是省/市/区任意一级）
        String province = null;
        String city = null;
        String district = null;
        
        if (location != null && !location.trim().isEmpty()) {
            // 这里简单处理，实际可能需要根据地区编码或者格式判断是哪一级
            if (location.endsWith("省") || location.endsWith("自治区") || location.endsWith("市") && (location.equals("北京市") || location.equals("天津市") || location.equals("上海市") || location.equals("重庆市"))) {
                province = location;
            } else if (location.endsWith("市") || location.endsWith("盟") || location.endsWith("自治州")) {
                city = location;
            } else {
                district = location;
            }
        }
        
        // 构建查询条件
        LambdaQueryWrapper<LaborDemand> queryWrapper = new LambdaQueryWrapper<>();
        
        // 项目类型条件
        if (projectType != null) {
            // 查找项目类型对应的项目ID列表
            List<Integer> projectIds = projectMapper.getProjectIdsByType(projectType);
            if (!projectIds.isEmpty()) {
                queryWrapper.in(LaborDemand::getProjectId, projectIds);
            } else {
                // 如果没有符合条件的项目，直接返回空列表
                return new Page<>();
            }
        }
        
        // 工种条件
        if (occupationId != null) {
            queryWrapper.eq(LaborDemand::getJobTypeId, occupationId);
        }
        
        // 地区条件 - 这里需要关联项目表，因为地区信息通常存储在项目表中
        if (province != null || city != null || district != null) {
            List<Integer> projectIdsByLocation = projectMapper.getProjectIdsByLocation(province, city, district);
            if (!projectIdsByLocation.isEmpty()) {
                // 如果已经有项目类型过滤条件，需要取交集
                if (projectType != null) {
                    queryWrapper.and(wrapper -> wrapper.in(LaborDemand::getProjectId, projectIdsByLocation));
                } else {
                    queryWrapper.in(LaborDemand::getProjectId, projectIdsByLocation);
                }
            } else {
                // 如果没有符合地区条件的项目，直接返回空列表
                return new Page<>();
            }
        }
        
        // 只查询状态为"open"的需求
        queryWrapper.eq(LaborDemand::getStatus, "open");
        
        // 执行查询 - 使用与实体类匹配的分页对象
        Page<LaborDemand> demandPage = this.page(pageObj, queryWrapper);
        
        // 转换为VO对象
        Page<LaborDemandVO> resultPage = new Page<>();
        resultPage.setCurrent(demandPage.getCurrent());
        resultPage.setSize(demandPage.getSize());
        resultPage.setTotal(demandPage.getTotal());
        
        List<LaborDemandVO> voList = demandPage.getRecords().stream().map(demand -> {
            // 获取关联数据并构建VO
            return laborDemandMapper.getLaborDemandById(demand.getId());
        }).filter(vo -> vo != null).collect(Collectors.toList());
        
        resultPage.setRecords(voList);
        
        return resultPage;
    }
}
