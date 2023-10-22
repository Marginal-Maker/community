package com.majing.community;

import com.majing.community.util.RegularExpressionUtil;
import org.junit.jupiter.api.Test;

/**
 * @author majing
 * @date 2023-10-22 16:50
 * @Description
 */
public class UtilTest {
    @Test
    public void testRegularExpression(){
        System.out.println(RegularExpressionUtil.verifyPassword("Ma12181014!"));
    }
}
