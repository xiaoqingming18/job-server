package com.qingming.jobserver.service;

import com.qingming.jobserver.model.dao.user.AdminLoginDao;
import com.qingming.jobserver.model.dao.user.CompanyAdminLoginDao;
import com.qingming.jobserver.model.dao.user.JobSeekerLoginDao;
import com.qingming.jobserver.model.dao.user.JobSeekerRegisterDao;
import com.qingming.jobserver.model.dao.user.JobSeekerUpdateInfoDao;
import com.qingming.jobserver.model.dao.user.ProjectManagerLoginDao;
import com.qingming.jobserver.model.dao.user.UpdatePasswordDao;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.CompanyInfoVO;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;

public interface UserService {
    User getByUsername(String username);
    User getByMobile(String mobile);
    User getByEmail(String email);
    User getByUsernameAndPassword(String username, String password);

    User registerJobSeeker(JobSeekerRegisterDao registerDao);
    
    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 是否更新成功
     */
    boolean updateUserAvatar(Long userId, String avatarUrl);

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
    JobSeekerProfileVO getJobSeekerProfile(Long userId);    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param updatePasswordDao 修改密码请求数据
     * @return 是否更新成功
     */
    boolean updatePassword(Long userId, UpdatePasswordDao updatePasswordDao);
    
    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getById(Long userId);
    
    /**
     * 求职者登录
     * @param loginDao 登录信息DAO对象
     * @return 登录成功的用户信息
     */
    User jobSeekerLogin(JobSeekerLoginDao loginDao);
    
    /**
     * 企业管理员登录
     * @param loginDao 登录信息DAO对象
     * @return 登录成功的用户信息
     */
    User companyAdminLogin(CompanyAdminLoginDao loginDao);
    
    /**
     * 项目经理登录
     * @param loginDao 登录信息DAO对象
     * @return 登录成功的用户信息
     */
    User projectManagerLogin(ProjectManagerLoginDao loginDao);

    /**
     * 系统管理员登录
     * @param loginDao 登录信息DAO对象
     * @return 登录成功的用户信息
     */
    User adminLogin(AdminLoginDao loginDao);

    /**
     * 获取用户所属企业信息
     * @param userId 用户ID
     * @return 企业信息VO，如果用户不属于任何企业则返回null
     */
    CompanyInfoVO getUserCompanyInfo(Long userId);
}