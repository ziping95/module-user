package com.wzp.module.user.bean;

import com.wzp.module.core.base.bean.AbstractBaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends AbstractBaseBean {

    private String loginId;

    private String password;

    private Role role;

    private Date lastLoginDate;

    private String ip;

    private String photo;

    private String trueName;

    private String mobilePhone;

    private String email;

    private String nickName;

    private String gender;

}
