package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.entity.User;

public interface UserService {
    User getByUsername(String username);
    User getByMobile(String mobile);
    User getByEmail(String email);
    User getByUsernameAndPassword(String username, String password);

    User registerJobSeeker(JobSeekerRegisterDao registerDao);

    /**
     * 更新求职者资料
     * @param updateInfo 包含更新信息的DAO对象
     * @return 更新后的用户对象
     */
    User updateJobSeekerProfile(JobSeekerUpdateInfoDao updateInfo);
}