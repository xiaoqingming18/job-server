package com.qingming.jobserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.model.dao.JobQueryDao;
import com.qingming.jobserver.model.vo.JobVO;
import com.qingming.jobserver.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 分页查询职位列表
     *
     * @param query 查询条件
     * @return 职位列表
     */
    @PostMapping("/list")
    public BaseResponse<IPage<JobVO>> queryJobList(@RequestBody JobQueryDao query) {
        IPage<JobVO> result = jobService.queryJobList(query);
        return ResultUtils.success(result);
    }

    /**
     * 获取热门工种列表
     *
     * @param limit 返回数量限制
     * @return 热门工种列表
     */
    @GetMapping("/hot-types")
    public BaseResponse<List<JobVO>> queryHotJobTypes(@RequestParam(defaultValue = "10") Integer limit) {
        List<JobVO> result = jobService.queryHotJobTypes(limit);
        return ResultUtils.success(result);
    }

    /**
     * 获取职位详情
     *
     * @param id 职位ID
     * @return 职位详情
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<JobVO> getJobDetail(@PathVariable Integer id) {
        JobVO result = jobService.getJobDetail(id);
        return ResultUtils.success(result);
    }

    /**
     * 获取推荐职位列表
     *
     * @param limit 返回数量限制
     * @return 推荐职位列表
     */
    @GetMapping("/recommend")
    public BaseResponse<List<JobVO>> queryRecommendJobs(@RequestParam(defaultValue = "10") Integer limit) {
        // 从当前登录用户中获取用户ID
        Long userId = 1L; // TODO: 从登录用户中获取
        List<JobVO> result = jobService.queryRecommendJobs(userId, limit);
        return ResultUtils.success(result);
    }
} 