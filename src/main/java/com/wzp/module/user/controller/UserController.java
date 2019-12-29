package com.wzp.module.user.controller;

import com.wzp.module.core.utils.FileUtil;
import com.wzp.module.core.utils.RedisUtil;
import com.wzp.module.core.utils.StringUtil;
import com.wzp.module.user.bean.Role;
import com.wzp.module.user.bean.User;
import com.wzp.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/test")
    public String create() {
        User user = new User();
        user.setTrueName("测试事务");
        Role role = new Role();
        role.setId("123");
        user.setLoginId("123");
        user.setPassword(DigestUtils.md5DigestAsHex("1234".getBytes(StandardCharsets.UTF_8)));
        user.setRole(role);
        try {
            userService.create(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        RedisUtil.put("user",user,60);
        log.info("从缓存中拿到的user对象为：{},缓存剩余时间：{}秒",RedisUtil.get("user"),RedisUtil.getExpire("user"));
        return user.getId();
    }

    public static void main(String[] args) {
        System.out.println(FileUtil.getClassPath());
    }
}
