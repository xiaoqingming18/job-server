package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.OccupationCategoryMapper;
import com.qingming.jobserver.mapper.OccupationMapper;
import com.qingming.jobserver.model.dao.occupation.OccupationAddRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationStatusUpdateRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationUpdateRequest;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.model.entity.OccupationCategory;
import com.qingming.jobserver.service.OccupationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 工种服务实现类
 */
@Service
public class OccupationServiceImpl implements OccupationService {

    @Autowired
    private OccupationMapper occupationMapper;
    
    @Autowired
    private OccupationCategoryMapper categoryMapper;

    @Override
    @Transactional
    public Occupation addOccupation(OccupationAddRequest occupationAddRequest) {
        // 检查同名工种是否已存在
        Occupation existingOccupation = occupationMapper.selectByName(occupationAddRequest.getName());
        if (existingOccupation != null) {
            throw new BusinessException(40001, "工种名称已存在");
        }
        
        // 检查类别是否存在
        OccupationCategory category = categoryMapper.selectById(occupationAddRequest.getCategoryId());
        if (category == null) {
            throw new BusinessException(40400, "指定的工种类别不存在");
        }

        Occupation occupation = new Occupation();
        BeanUtils.copyProperties(occupationAddRequest, occupation);
        occupation.setStatus(1); // 默认状态为启用
        occupation.setCreateTime(new Date());

        // 插入数据库
        occupationMapper.insert(occupation);
        return occupation;
    }

    @Override
    @Transactional
    public Occupation updateOccupation(OccupationUpdateRequest occupationUpdateRequest) {
        // 检查工种是否存在
        Occupation occupation = occupationMapper.selectById(occupationUpdateRequest.getId());
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }

        // 如果修改了名称，需要检查是否与其他工种重名
        if (StringUtils.hasText(occupationUpdateRequest.getName()) && 
            !occupationUpdateRequest.getName().equals(occupation.getName())) {
            Occupation existingOccupation = occupationMapper.selectByName(occupationUpdateRequest.getName());
            if (existingOccupation != null && !Objects.equals(existingOccupation.getId(), occupationUpdateRequest.getId())) {
                throw new BusinessException(40001, "工种名称已存在");
            }
        }
        
        // 如果修改了类别，需要检查类别是否存在
        if (occupationUpdateRequest.getCategoryId() != null && 
            !Objects.equals(occupationUpdateRequest.getCategoryId(), occupation.getCategoryId())) {
            OccupationCategory category = categoryMapper.selectById(occupationUpdateRequest.getCategoryId());
            if (category == null) {
                throw new BusinessException(40400, "指定的工种类别不存在");
            }
        }

        // 更新非空字段
        if (StringUtils.hasText(occupationUpdateRequest.getName())) {
            occupation.setName(occupationUpdateRequest.getName());
        }
        
        if (occupationUpdateRequest.getCategoryId() != null) {
            occupation.setCategoryId(occupationUpdateRequest.getCategoryId());
        }
        
        if (occupationUpdateRequest.getIcon() != null) {
            occupation.setIcon(occupationUpdateRequest.getIcon());
        }
        
        if (occupationUpdateRequest.getDescription() != null) {
            occupation.setDescription(occupationUpdateRequest.getDescription());
        }
        
        if (occupationUpdateRequest.getRequiredCertificates() != null) {
            occupation.setRequiredCertificates(occupationUpdateRequest.getRequiredCertificates());
        }
        
        if (occupationUpdateRequest.getAverageDailyWage() != null) {
            occupation.setAverageDailyWage(occupationUpdateRequest.getAverageDailyWage());
        }
        
        if (occupationUpdateRequest.getDifficultyLevel() != null) {
            occupation.setDifficultyLevel(occupationUpdateRequest.getDifficultyLevel());
        }
        
        occupation.setUpdateTime(new Date());
        
        // 更新数据库
        occupationMapper.updateById(occupation);
        return occupation;
    }

    @Override
    @Transactional
    public Occupation updateOccupationStatus(OccupationStatusUpdateRequest statusUpdateRequest) {
        // 检查工种是否存在
        Occupation occupation = occupationMapper.selectById(statusUpdateRequest.getId());
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }
        
        // 验证状态值是否有效
        if (statusUpdateRequest.getStatus() != 0 && statusUpdateRequest.getStatus() != 1) {
            throw new BusinessException(40001, "无效的状态值，应为0或1");
        }
        
        occupation.setStatus(statusUpdateRequest.getStatus());
        occupation.setUpdateTime(new Date());
        
        // 更新数据库
        occupationMapper.updateById(occupation);
        return occupation;
    }

    @Override
    public Occupation getOccupationById(Integer id) {
        Occupation occupation = occupationMapper.selectByIdWithCategory(id);
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }
        return occupation;
    }

    @Override
    public Map<String, Object> pageOccupations(Integer page, Integer size, Integer categoryId) {
        // 创建分页对象
        Page<Occupation> pageParam = new Page<>(page, size);
        
        // 执行分页查询
        IPage<Occupation> occupationPage = occupationMapper.selectPageWithCategory(pageParam, categoryId);
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", occupationPage.getTotal());
        result.put("pages", occupationPage.getPages());
        result.put("current", occupationPage.getCurrent());
        result.put("size", occupationPage.getSize());
        result.put("records", occupationPage.getRecords());
        
        return result;
    }

    @Override
    public List<Occupation> listOccupationsByCategoryId(Integer categoryId) {
        if (categoryId == null) {
            throw new BusinessException(40001, "类别ID不能为空");
        }
        return occupationMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<Occupation> listOccupationsByWageRange(BigDecimal minWage, BigDecimal maxWage) {
        LambdaQueryWrapper<Occupation> queryWrapper = new LambdaQueryWrapper<>();
        
        // 设置查询条件
        if (minWage != null) {
            queryWrapper.ge(Occupation::getAverageDailyWage, minWage);
        }
        
        if (maxWage != null) {
            queryWrapper.le(Occupation::getAverageDailyWage, maxWage);
        }
        
        return occupationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Occupation> listOccupationsByDifficultyLevel(Integer level) {
        if (level == null || level < 1 || level > 5) {
            throw new BusinessException(40001, "难度等级必须在1-5之间");
        }
        
        LambdaQueryWrapper<Occupation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Occupation::getDifficultyLevel, level);
        
        return occupationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Occupation> listHotOccupations(Integer limit) {
        LambdaQueryWrapper<Occupation> queryWrapper = Wrappers.<Occupation>lambdaQuery()
                .eq(Occupation::getStatus, 1) // 只查询启用状态的工种
                .orderByDesc(Occupation::getAverageDailyWage)
                .last("limit " + limit);
        
        return occupationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Occupation> searchOccupations(String name, Integer categoryId, BigDecimal minWage, BigDecimal maxWage, Integer difficultyLevel) {
        LambdaQueryWrapper<Occupation> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加各个查询条件
        if (StringUtils.hasText(name)) {
            queryWrapper.like(Occupation::getName, "%" + name + "%");
        }
        
        if (categoryId != null) {
            queryWrapper.eq(Occupation::getCategoryId, categoryId);
        }
        
        if (minWage != null) {
            queryWrapper.ge(Occupation::getAverageDailyWage, minWage);
        }
        
        if (maxWage != null) {
            queryWrapper.le(Occupation::getAverageDailyWage, maxWage);
        }
        
        if (difficultyLevel != null) {
            queryWrapper.eq(Occupation::getDifficultyLevel, difficultyLevel);
        }
        
        // 默认只查询启用状态的工种
        queryWrapper.eq(Occupation::getStatus, 1);
        
        return occupationMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public boolean deleteOccupation(Integer id) {
        Occupation occupation = occupationMapper.selectById(id);
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }
        
        int result = occupationMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public Occupation getOccupationByName(String name) {
        return occupationMapper.selectByName(name);
    }
}