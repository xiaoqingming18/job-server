package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.occupation.OccupationAddRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationStatusUpdateRequest;
import com.qingming.jobserver.model.dao.occupation.OccupationUpdateRequest;
import com.qingming.jobserver.model.entity.Occupation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工种服务接口
 */
public interface OccupationService {

    /**
     * 添加工种
     * @param occupationAddRequest 添加工种请求
     * @return 添加后的工种对象
     */
    Occupation addOccupation(OccupationAddRequest occupationAddRequest);

    /**
     * 更新工种信息
     * @param occupationUpdateRequest 更新工种请求
     * @return 更新后的工种对象
     */
    Occupation updateOccupation(OccupationUpdateRequest occupationUpdateRequest);

    /**
     * 更新工种状态
     * @param statusUpdateRequest 更新工种状态请求
     * @return 更新后的工种对象
     */
    Occupation updateOccupationStatus(OccupationStatusUpdateRequest statusUpdateRequest);

    /**
     * 根据ID获取工种详情（包含类别名称）
     * @param id 工种ID
     * @return 工种对象
     */
    Occupation getOccupationById(Integer id);

    /**
     * 分页获取工种列表（包含类别名称）
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 工种类别ID（可选）
     * @return 工种分页结果
     */
    Map<String, Object> pageOccupations(Integer page, Integer size, Integer categoryId);

    /**
     * 根据类别ID获取工种列表
     * @param categoryId 类别ID
     * @return 工种列表
     */
    List<Occupation> listOccupationsByCategoryId(Integer categoryId);

    /**
     * 根据薪资范围查询工种
     * @param minWage 最低薪资（可选）
     * @param maxWage 最高薪资（可选）
     * @return 符合条件的工种列表
     */
    List<Occupation> listOccupationsByWageRange(BigDecimal minWage, BigDecimal maxWage);

    /**
     * 根据难度等级查询工种
     * @param level 难度等级(1-5)
     * @return 符合条件的工种列表
     */
    List<Occupation> listOccupationsByDifficultyLevel(Integer level);

    /**
     * 获取热门工种（按平均日薪排序，取前N个）
     * @param limit 返回数量
     * @return 热门工种列表
     */
    List<Occupation> listHotOccupations(Integer limit);

    /**
     * 工种搜索，支持多条件组合查询
     * @param name 工种名称关键词（可选）
     * @param categoryId 工种类别ID（可选）
     * @param minWage 最低日薪（可选）
     * @param maxWage 最高日薪（可选）
     * @param difficultyLevel 难度等级（可选）
     * @return 符合条件的工种列表
     */
    List<Occupation> searchOccupations(String name, Integer categoryId, BigDecimal minWage, BigDecimal maxWage, Integer difficultyLevel);

    /**
     * 根据ID删除工种
     * @param id 工种ID
     * @return 是否删除成功
     */
    boolean deleteOccupation(Integer id);

    /**
     * 根据名称查询工种
     * @param name 工种名称
     * @return 工种对象
     */
    Occupation getOccupationByName(String name);
}