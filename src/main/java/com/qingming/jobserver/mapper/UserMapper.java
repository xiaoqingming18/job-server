package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.User;
import com.qingming.jobserver.model.vo.JobSeekerProfileVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
    User selectByMobile(String mobile);
    User selectByEmail(String email);

    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    int insertJobSeekerUser(@Param("params") Map<String, Object> params);
    
    /**
     * 插入求职者扩展信息
     * @param params 包含用户ID的Map对象
     * @return 影响行数
     */
    int insertJobSeekerExt(@Param("params") Map<String, Object> params);    /**
     * 动态更新用户基本信息（头像、手机号、邮箱等）
     * @param params 包含更新字段的Map对象
     * @return 影响行数
     */
    int updateUserBasicInfo(@Param("params") Map<String, Object> params);
    
    /**
     * 动态更新求职者扩展信息
     * @param params 包含更新字段的Map对象
     * @return 影响行数
     */
    int updateJobSeeker(@Param("params") Map<String, Object> params);
    
    /**
     * 获取求职者完整资料，包括用户基本信息和求职者详细信息
     * @param userId 用户ID
     * @return 求职者完整资料VO对象
     */
    JobSeekerProfileVO getJobSeekerProfile(@Param("userId") Long userId);

    /**
     * 更新用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
    
    /**
     * 获取最新的用户ID
     * @return 最新的用户ID，如果没有用户则返回0
     */
    Long getLatestUserId();
    
    /**
     * 插入企业管理员用户
     * @param params 包含用户信息的Map对象
     * @return 影响行数
     */
    int insertProjectManagerUser(@Param("params") Map<String, Object> params);
}