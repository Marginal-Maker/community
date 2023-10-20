package com.majing.community.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * @author majing
 * @date 2023-09-17 19:53
 * @Description
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest httpServletRequest, String name){
        if(httpServletRequest == null || StringUtils.isBlank(name)){
            throw new IllegalArgumentException("参数不正确");
        }
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
