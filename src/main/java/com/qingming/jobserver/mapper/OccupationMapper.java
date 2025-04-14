package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.Occupation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工种Mapper接口
 */
@Mapper
public interface OccupationMapper extends BaseMapper<Occupation> {
    
    /**
     * 根据类别查询工种列表
     * @param category 工种类别
     * @return 工种列表
     */
    List<Occupation> selectByCategory(@Param("category") String category);
    
    /**
     * 根据名称查询工种
     * @param name 工种名称
     * @return 工种对象
     */
    Occupation selectByName(@Param("name") String name);
}