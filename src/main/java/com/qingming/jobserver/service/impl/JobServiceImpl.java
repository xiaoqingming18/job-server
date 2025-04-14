package com.qingming.jobserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.mapper.JobMapper;
import com.qingming.jobserver.model.dao.JobQueryDao;
import com.qingming.jobserver.model.vo.JobVO;
import com.qingming.jobserver.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public IPage<JobVO> queryJobList(JobQueryDao query) {
        Page<JobVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return jobMapper.queryJobList(page, query);
    }

    @Override
    public List<JobVO> queryHotJobTypes(Integer limit) {
        return jobMapper.queryHotJobTypes(limit);
    }

    @Override
    public JobVO getJobDetail(Integer id) {
        return jobMapper.getJobDetail(id);
    }

    @Override
    public List<JobVO> queryRecommendJobs(Long userId, Integer limit) {
        return jobMapper.queryRecommendJobs(userId, limit);
    }
} 