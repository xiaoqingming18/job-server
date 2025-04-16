package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.OccupationCategoryMapper;
import com.qingming.jobserver.mapper.OccupationMapper;
import com.qingming.jobserver.model.dao.occupation.CategoryAddRequest;
import com.qingming.jobserver.model.dao.occupation.CategoryUpdateRequest;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.model.entity.OccupationCategory;
import com.qingming.jobserver.service.OccupationCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 工种类别服务实现类
 */
@Service
public class OccupationCategoryServiceImpl implements OccupationCategoryService {

    @Autowired
    private OccupationCategoryMapper occupationCategoryMapper;
    
    @Autowired
    private OccupationMapper occupationMapper;

    @Override
    @Transactional
    public OccupationCategory addCategory(CategoryAddRequest categoryAddRequest) {
        // 检查同名类别是否已存在
        OccupationCategory existingCategory = occupationCategoryMapper.selectByName(categoryAddRequest.getName());
        if (existingCategory != null) {
            throw new BusinessException(40001, "工种类别名称已存在");
        }

        OccupationCategory category = new OccupationCategory();
        BeanUtils.copyProperties(categoryAddRequest, category);
        category.setCreateTime(new Date());

        // 插入数据库
        occupationCategoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public OccupationCategory updateCategory(CategoryUpdateRequest categoryUpdateRequest) {
        // 检查类别是否存在
        OccupationCategory category = occupationCategoryMapper.selectById(categoryUpdateRequest.getId());
        if (category == null) {
            throw new BusinessException(40400, "未找到指定工种类别");
        }

        // 如果修改了名称，需要检查是否与其他类别重名
        if (StringUtils.hasText(categoryUpdateRequest.getName()) && 
            !categoryUpdateRequest.getName().equals(category.getName())) {
            OccupationCategory existingCategory = occupationCategoryMapper.selectByName(categoryUpdateRequest.getName());
            if (existingCategory != null && !existingCategory.getId().equals(categoryUpdateRequest.getId())) {
                throw new BusinessException(40001, "工种类别名称已存在");
            }
        }

        // 更新非空字段
        if (StringUtils.hasText(categoryUpdateRequest.getName())) {
            category.setName(categoryUpdateRequest.getName());
        }
        
        if (categoryUpdateRequest.getIcon() != null) {
            category.setIcon(categoryUpdateRequest.getIcon());
        }
        
        if (categoryUpdateRequest.getDescription() != null) {
            category.setDescription(categoryUpdateRequest.getDescription());
        }
        
        if (categoryUpdateRequest.getSortOrder() != null) {
            category.setSortOrder(categoryUpdateRequest.getSortOrder());
        }
        
        category.setUpdateTime(new Date());
        
        // 更新数据库
        occupationCategoryMapper.updateById(category);
        return category;
    }

    @Override
    public OccupationCategory getCategoryById(Integer id) {
        OccupationCategory category = occupationCategoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(40400, "未找到指定工种类别");
        }
        return category;
    }

    @Override
    public List<OccupationCategory> listAllCategories() {
        LambdaQueryWrapper<OccupationCategory> queryWrapper = Wrappers.<OccupationCategory>lambdaQuery()
                .orderByAsc(OccupationCategory::getSortOrder);
        return occupationCategoryMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Integer id) {
        // 检查类别是否存在
        OccupationCategory category = occupationCategoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(40400, "未找到指定工种类别");
        }
        
        // 检查是否有工种关联此类别
        LambdaQueryWrapper<Occupation> queryWrapper = Wrappers.<Occupation>lambdaQuery()
                .eq(Occupation::getCategoryId, id);
        long count = occupationMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(40001, "该类别下存在工种，无法删除");
        }
        
        int result = occupationCategoryMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public OccupationCategory getCategoryByName(String name) {
        return occupationCategoryMapper.selectByName(name);
    }
} 