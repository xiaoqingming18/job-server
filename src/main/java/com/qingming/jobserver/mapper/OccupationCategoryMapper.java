package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.OccupationCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 工种类别Mapper接口
 */
@Mapper
public interface OccupationCategoryMapper extends BaseMapper<OccupationCategory> {
    
    /**
     * 根据名称查询工种类别
     * @param name 类别名称
     * @return 工种类别对象
     */
    OccupationCategory selectByName(@Param("name") String name);
} 