package com.majing.community.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author majing
 * @date 2023-09-18 09:54
 * @Description
 */
public class RegularExpressionUtil {
    public static String verifyPassword(String password){
        if(password.length() < 8){
            return "密码不能小于8位";
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()){
            return "密码必须同时包含数字，大小写字母和特殊字符（!@#$%^&+=）！";
        }
        return null;
    }
}
