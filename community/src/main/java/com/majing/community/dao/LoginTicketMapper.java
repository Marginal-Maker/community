package com.majing.community.dao;

import com.majing.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author majing
 * @date 2023-09-13 15:41
 * @Description 登录凭证表的操作
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
    /**
     * 插入登录凭证
     * @param loginTicket
     * @return java.lang.Integer
     * @created at 2023/9/13 15:42
    */
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id" )
    Integer insertLoginTicket(LoginTicket loginTicket);
    /**
     * 根据当前凭证查询
     * @param ticket
     * @return com.majing.community.entity.LoginTicket
     * @created at 2023/9/13 15:43
    */
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);
    /**
     * 更新status，当做删除，一般公司不做真删除
     * @param ticket
     * @param status
     * @return java.lang.Integer
     * @created at 2023/9/13 15:44
    */
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    Integer updateStatus(String ticket, Integer status);
}
