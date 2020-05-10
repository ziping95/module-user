package com.wzp.module.user.controller;

import com.wzp.module.core.dto.ResultDataModel;
import com.wzp.module.core.utils.RedisUtil;
import com.wzp.module.user.UserConstant;
import com.wzp.module.user.bean.Role;
import com.wzp.module.user.bean.User;
import com.wzp.module.user.mapper.UserMapper;
import com.wzp.module.user.service.UserService;
import javafx.application.ConditionalFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @RequestMapping(value = "/manage/user/test")
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

    @RequestMapping(value = "/manage/tts/test")
    public String userTest() {
        return UserConstant.getCurrentUser().getId();
    }

    @RequestMapping(value = "/open/test",method = RequestMethod.GET)
    public ResultDataModel openTest(@RequestParam("type") Integer type) {

        if(type == 0) {
            Integer count = userMapper.list().size();
            return ResultDataModel.handleSuccessResult(count);
        } else if (type == 1) {
            Integer count = userMapper.delete("fa5bad87403811eab87052540028f465");
            return ResultDataModel.handleSuccessResult(count);
        } else if (type == 2) {
            List ids = new ArrayList();
            ids.add("b95b0676403511eab87052540028f465");
            ids.add("fa5ba7b0403811eab87052540028f465");
            Integer count = userMapper.batchDelete(ids);
            return ResultDataModel.handleSuccessResult(count);
        } else if (type == 3) {
            User user = new User();
            user.setId("753e43da2a3911ea9e0352540028f465");
            Role role = new Role();
            role.setId("123987456");
            user.setLoginId("18630266723");
            user.setRole(role);
            Integer count = userMapper.update(user);
            return ResultDataModel.handleSuccessResult(count);
        }
        return ResultDataModel.handleFailureResult();
    }

    @RequestMapping(value = "/open/test/redis",method = RequestMethod.GET)
    public ResultDataModel openTest() {
        return ResultDataModel.handleSuccessResult(RedisUtil.get("test_4"));
    }

    int i = 0;
    public void append() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("1");
        stringBuffer.append("2");
        stringBuffer.append("3");
        new Object().notify();
    }
}
