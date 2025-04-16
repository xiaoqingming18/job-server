package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 根据类别ID查询工种列表
     * @param categoryId 类别ID
     * @return 工种列表
     */
    List<Occupation> selectByCategoryId(@Param("categoryId") Integer categoryId);
    
    /**
     * 根据名称查询工种
     * @param name 工种名称
     * @return 工种对象
     */
    Occupation selectByName(@Param("name") String name);
    
    /**
     * 分页查询工种并关联类别信息
     * @param page 分页参数
     * @param categoryId 类别ID（可选）
     * @return 工种分页结果（含类别名称）
     */
    IPage<Occupation> selectPageWithCategory(Page<Occupation> page, @Param("categoryId") Integer categoryId);
    
    /**
     * 根据ID查询工种（含类别名称）
     * @param id 工种ID
     * @return 工种对象（含类别名称）
     */
    Occupation selectByIdWithCategory(@Param("id") Integer id);
}