package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.entity.User;

public interface UserService {
    User getByUsername(String username);
    User getByMobile(String mobile);
    User getByEmail(String email);
    User getByUsernameAndPassword(String username, String password);

    User registerJobSeeker(JobSeekerRegisterDao registerDao);
}