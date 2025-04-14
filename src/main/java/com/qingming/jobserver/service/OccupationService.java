package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.occupation.AddOccupationDao;
import com.qingming.jobserver.model.dao.occupation.UpdateOccupationDao;
import com.qingming.jobserver.model.entity.Occupation;

import java.util.List;

/**
 * 工种服务接口
 */
public interface OccupationService {

    /**
     * 添加工种
     * @param addOccupationDao 添加工种请求对象
     * @return 添加后的工种对象
     */
    Occupation addOccupation(AddOccupationDao addOccupationDao);

    /**
     * 更新工种信息
     * @param updateOccupationDao 更新工种请求对象
     * @return 更新后的工种对象
     */
    Occupation updateOccupation(UpdateOccupationDao updateOccupationDao);

    /**
     * 根据ID获取工种详情
     * @param id 工种ID
     * @return 工种对象
     */
    Occupation getOccupationById(Integer id);

    /**
     * 获取所有工种列表
     * @return 工种列表
     */
    List<Occupation> listOccupations();

    /**
     * 根据工种类别查询工种列表
     * @param category 工种类别
     * @return 工种列表
     */
    List<Occupation> listOccupationsByCategory(String category);

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