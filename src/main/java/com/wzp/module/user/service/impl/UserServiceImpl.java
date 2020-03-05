package com.wzp.module.user.service.impl;

import com.wzp.module.core.utils.CollectionUtil;
import com.wzp.module.user.bean.User;
import com.wzp.module.user.mapper.UserMapper;
import com.wzp.module.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    //获取事务源
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Resource
    private UserMapper userMapper;

    @Override
    public void create(User user) throws Exception {
        TransactionStatus transactionStatus = null;

            transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());

            this.userMapper.create(user);

        if(transactionStatus != null){
            dataSourceTransactionManager.rollback(transactionStatus);

        }
    }

    @Override
    public List<User> list() throws Exception {
        return this.userMapper.list();
    }

    @Override
    public User selectUserByLoginId(String loginId) {
        Map<String,Object> map = new HashMap<>();
        map.put("loginId",loginId);
        List<User> list = this.userMapper.detailsByCondition(map);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    private void quartzTest() {
        Map<String,Object> map = new HashMap<>();
        map.put("loginId","18630266723");
        System.out.println(userMapper.detailsByCondition(map).get(0));
    }

    public void quartzCreate(String s) throws Exception {
        String str = null;
        str.equals("");
    }
}
