package com.wzp.module.user.bean;

import com.wzp.module.core.base.bean.AbstractBaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends AbstractBaseBean {

    private String name;

}
