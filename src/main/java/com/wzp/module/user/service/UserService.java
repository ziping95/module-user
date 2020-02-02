package com.wzp.module.user.service;

import com.wzp.module.user.bean.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {

    void create(User user) throws Exception;

    User selectUserByLoginId(@Param("loginId") String loginId);

}
