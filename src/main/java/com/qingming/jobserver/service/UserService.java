package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;

public interface UserService {
    User getByUsername(String username);
    User getByMobile(String mobile);
    User getByEmail(String email);
    User getByUsernameAndPassword(String username, String password);

    User registerJobSeeker(JobSeekerRegisterDao registerDao);

    /**
     * 更新求职者资料
     * @param updateInfo 包含更新信息的DAO对象
     * @return 更新后的求职者完整资料VO
     */
    JobSeekerProfileVO updateJobSeekerProfile(JobSeekerUpdateInfoDao updateInfo);
    
    /**
     * 获取求职者完整资料
     * @param userId 用户ID
     * @return 求职者完整资料VO对象
     */
    JobSeekerProfileVO getJobSeekerProfile(Long userId);
}