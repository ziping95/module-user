package com.wzp.module.user.mapper;

import com.wzp.module.user.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    Integer create(User user);

    /**
     * 依据Id查询
     * @param id
     * @return
     */
    User details(@Param("id") String id);

    /**
     * 自定义条件查询
     * @param map
     * @return
     */
    List<User> detailsByCondition(Map<String,Object> map);

    /**
     * 依据ID更新
     * @param user
     * @return
     */
    Integer update(User user);

    /**
     * 依据ID删除
     * @param id
     * @return
     */
    Integer delete(@Param("id") String id);

    /**
     * 查询列表
     * @return
     */
    List<User> list();

    /**
     * 批次添加
     * @param userList
     * @return
     */
    Integer batchCreate(List<User> userList);

    /**
     * 批次删除
     * @param ids
     * @return
     */
    Integer batchDelete(List<String> ids);

    /**
     * 批次更新
     * @param userList
     * @return
     */
    Integer batchUpdate(List<User> userList);
}
