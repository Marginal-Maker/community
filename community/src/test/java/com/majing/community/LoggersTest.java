package com.majing.community;

import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author majing
 * @date 2023-08-03 12:25
 * @Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggersTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggersTest.class);
    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debug log");
        logger.info("info log");
        logger.error("error log");
    }

}
