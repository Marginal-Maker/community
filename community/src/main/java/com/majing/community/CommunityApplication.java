package com.majing.community;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author majing
 * @date 2023-08-03 12:25
 * @Description 配置文件
 */
@SpringBootApplication
@ServletComponentScan
public class CommunityApplication {
	public static void main(String[] args) {
		//启动tomcat，自动创建Spring容器，扫描配置类所在的包以及子包下的类
		SpringApplication.run(CommunityApplication.class, args);
	}

}
