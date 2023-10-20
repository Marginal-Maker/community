package com.majing.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author majing
 * @date 2023-09-12 14:40
 * @Description
 */
public class CommunityUtil {
    /**
     * 生成随机字符串(激活码，上传文件的文件名)
     * @param
     * @return java.lang.String
     * @created at 2023/9/12 14:51
    */
    public static String generateUUID(){
        //UUID生成器
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     * MD5加密(当密码过于简单时，加密结果并不具备安全性，此时可以加盐值处理)
     * @param key
     * @return java.lang.String
     * @created at 2023/9/12 14:53
    */
    public static String md5(String key){
        //md5加密，先判断是否为null
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
