package com.wzp.module.user;

import com.wzp.module.core.utils.RedisUtil;
import com.wzp.module.user.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class UserConstant {

    public final static String ROLE_REDIS_KEY = "init_role_path";
    public final static String TOKEN_REDIS_KEY = "token_";

    public static User getCurrentUser() {
        User user = null;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Cookie[] cookies = request.getCookies();
        // 先从缓存中获取
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                String token = cookie.getValue();
                user = (User) RedisUtil.get(TOKEN_REDIS_KEY + token);
                break;
            }
        }

        if(user == null) {
           user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return user;
    }

}
