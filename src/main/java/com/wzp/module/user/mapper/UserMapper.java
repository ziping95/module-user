package com.wzp.module.user.mapper;

import com.wzp.module.user.bean.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    void create(User user);

    User selectUserByLoginId(@Param("loginId") String loginId);
}
