package com.majing.community.util;

/**
 * @author majing
 * @date 2023-09-15 16:59
 * @Description
 */
public interface CommunityConstant {
    /**
     * 激活成功
     * @created at 2023/9/15 17:12
    */
    Integer ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     * @created at 2023/9/15 17:12
    */
    Integer ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     * @created at 2023/9/15 17:12
     */
    Integer ACTIVATION_FAILURE = 2;
    /**
     * 默认状态下超时时间
     * @created at 2023/9/15 17:12
     */
    Integer DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    /**
     * 记住我的情况下的超时时间
     * @created at 2023/9/15 17:12
     */
    Integer REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
    Integer ENTITY_TYPE_POST = 1;
    Integer ENTITY_TYPE_COMMENT = 2;
}
