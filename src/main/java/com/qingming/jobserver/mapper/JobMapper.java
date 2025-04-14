package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qingming.jobserver.model.dao.JobQueryDao;
import com.qingming.jobserver.model.vo.JobVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobMapper extends BaseMapper<JobVO> {
    /**
     * 分页查询职位列表
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 职位列表
     */
    IPage<JobVO> queryJobList(IPage<JobVO> page, @Param("query") JobQueryDao query);

    /**
     * 获取热门工种列表
     *
     * @param limit 返回数量限制
     * @return 热门工种列表
     */
    List<JobVO> queryHotJobTypes(@Param("limit") Integer limit);

    /**
     * 获取职位详情
     *
     * @param id 职位ID
     * @return 职位详情
     */
    JobVO getJobDetail(@Param("id") Integer id);

    /**
     * 获取推荐职位列表
     *
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 推荐职位列表
     */
    List<JobVO> queryRecommendJobs(@Param("userId") Long userId, @Param("limit") Integer limit);
} 