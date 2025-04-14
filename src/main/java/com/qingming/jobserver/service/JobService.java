package com.qingming.jobserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qingming.jobserver.model.dao.JobQueryDao;
import com.qingming.jobserver.model.vo.JobVO;

import java.util.List;

public interface JobService {
    /**
     * 分页查询职位列表
     *
     * @param query 查询条件
     * @return 职位列表
     */
    IPage<JobVO> queryJobList(JobQueryDao query);

    /**
     * 获取热门工种列表
     *
     * @param limit 返回数量限制
     * @return 热门工种列表
     */
    List<JobVO> queryHotJobTypes(Integer limit);

    /**
     * 获取职位详情
     *
     * @param id 职位ID
     * @return 职位详情
     */
    JobVO getJobDetail(Integer id);

    /**
     * 获取推荐职位列表
     *
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 推荐职位列表
     */
    List<JobVO> queryRecommendJobs(Long userId, Integer limit);
} 