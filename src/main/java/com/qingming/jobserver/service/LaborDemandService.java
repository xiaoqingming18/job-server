package com.qingming.jobserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.vo.LaborDemandVO;

import java.util.List;

/**
 * 劳务需求服务接口
 */
public interface LaborDemandService {
    
    /**
     * 添加劳务需求
     * @param laborDemandAddDao 劳务需求添加参数
     * @param userId 当前用户ID
     * @return 添加成功的劳务需求信息
     */
    LaborDemandVO addLaborDemand(LaborDemandAddDao laborDemandAddDao, Long userId);
    
    /**
     * 根据ID获取劳务需求详情
     * @param demandId 劳务需求ID
     * @return 劳务需求详细信息
     */
    LaborDemandVO getLaborDemandInfo(Integer demandId);
    
    /**
     * 更新劳务需求信息
     * @param laborDemandUpdateDao 劳务需求更新参数
     * @param userId 当前用户ID
     * @return 更新后的劳务需求信息
     */
    LaborDemandVO updateLaborDemand(LaborDemandUpdateDao laborDemandUpdateDao, Long userId);
    
    /**
     * 更新劳务需求状态
     * @param statusUpdateDao 劳务需求状态更新参数
     * @param userId 当前用户ID
     * @return 更新后的劳务需求信息
     */
    LaborDemandVO updateLaborDemandStatus(LaborDemandStatusUpdateDao statusUpdateDao, Long userId);
    
    /**
     * 删除劳务需求
     * @param demandId 劳务需求ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    Boolean deleteLaborDemand(Integer demandId, Long userId);
    
    /**
     * 分页查询劳务需求
     * @param queryDao 查询参数
     * @return 劳务需求分页列表
     */
    Page<LaborDemandVO> queryLaborDemandList(LaborDemandQueryDao queryDao);
    
    /**
     * 获取特定项目的所有劳务需求
     * @param projectId 项目ID
     * @return 劳务需求列表
     */
    List<LaborDemandVO> getLaborDemandsByProjectId(Integer projectId);
    
    /**
     * 检查用户是否有权限对劳务需求进行修改操作
     * @param demandId 劳务需求ID
     * @param userId 用户ID
     * @return 是否有操作权限
     */
    Boolean checkOperationPermission(Integer demandId, Long userId);
}
