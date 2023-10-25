package com.majing.community;
import com.majing.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author majing
 * @date 2023-10-22 16:50
 * @Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UtilTest {
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    public void testRegularExpression(){
        System.out.println(sensitiveFilter.filter("fabc"));
    }
}
