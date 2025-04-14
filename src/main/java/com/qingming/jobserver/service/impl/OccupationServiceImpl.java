package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingming.jobserver.exception.BusinessException;
import com.qingming.jobserver.mapper.OccupationMapper;
import com.qingming.jobserver.model.dao.occupation.AddOccupationDao;
import com.qingming.jobserver.model.dao.occupation.UpdateOccupationDao;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.service.OccupationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 工种服务实现类
 */
@Service
public class OccupationServiceImpl implements OccupationService {

    @Autowired
    private OccupationMapper occupationMapper;

    @Override
    @Transactional
    public Occupation addOccupation(AddOccupationDao addOccupationDao) {
        // 检查同名工种是否已存在
        Occupation existingOccupation = occupationMapper.selectByName(addOccupationDao.getName());
        if (existingOccupation != null) {
            throw new BusinessException(40001, "工种名称已存在");
        }

        Occupation occupation = new Occupation();
        BeanUtils.copyProperties(addOccupationDao, occupation);
        occupation.setCreateTime(new Date());

        // 插入数据库
        occupationMapper.insert(occupation);
        return occupation;
    }

    @Override
    @Transactional
    public Occupation updateOccupation(UpdateOccupationDao updateOccupationDao) {
        // 检查工种是否存在
        Occupation occupation = occupationMapper.selectById(updateOccupationDao.getId());
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }

        // 如果修改了名称，需要检查是否与其他工种重名
        if (StringUtils.hasText(updateOccupationDao.getName()) && 
            !updateOccupationDao.getName().equals(occupation.getName())) {
            Occupation existingOccupation = occupationMapper.selectByName(updateOccupationDao.getName());
            if (existingOccupation != null && !Objects.equals(existingOccupation.getId(), updateOccupationDao.getId())) {
                throw new BusinessException(40001, "工种名称已存在");
            }
        }

        // 更新非空字段
        if (StringUtils.hasText(updateOccupationDao.getName())) {
            occupation.setName(updateOccupationDao.getName());
        }
        
        if (StringUtils.hasText(updateOccupationDao.getCategory())) {
            occupation.setCategory(updateOccupationDao.getCategory());
        }
        
        if (updateOccupationDao.getDescription() != null) {
            occupation.setDescription(updateOccupationDao.getDescription());
        }
        
        if (updateOccupationDao.getRequiredCertificates() != null) {
            occupation.setRequiredCertificates(updateOccupationDao.getRequiredCertificates());
        }
        
        if (updateOccupationDao.getAverageDailyWage() != null) {
            occupation.setAverageDailyWage(updateOccupationDao.getAverageDailyWage());
        }
        
        occupation.setUpdateTime(new Date());
        
        // 更新数据库
        occupationMapper.updateById(occupation);
        return occupation;
    }

    @Override
    public Occupation getOccupationById(Integer id) {
        Occupation occupation = occupationMapper.selectById(id);
        if (occupation == null) {
            throw new BusinessException(40400, "未找到指定工种");
        }
        return occupation;
    }

    @Override
    public List<Occupation> listOccupations() {
        return occupationMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<Occupation> listOccupationsByCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return listOccupations();
        }
        return occupationMapper.selectByCategory(category);
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