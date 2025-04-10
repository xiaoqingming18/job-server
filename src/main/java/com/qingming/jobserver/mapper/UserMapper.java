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
    int insertJobSeekerExt(@Param("params") Map<String, Object> params);

    /**
     * 动态更新求职者信息
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
}