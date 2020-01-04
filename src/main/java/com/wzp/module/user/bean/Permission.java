package com.wzp.module.user.bean;

import com.wzp.module.core.base.bean.AbstractBaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends AbstractBaseBean {

    private String name;

    private String path;

}
