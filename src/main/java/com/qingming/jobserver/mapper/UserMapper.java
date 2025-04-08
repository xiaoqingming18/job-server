package com.qingming.jobserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingming.jobserver.model.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(String username);
    User selectByMobile(String mobile);
    User selectByEmail(String email);

    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}