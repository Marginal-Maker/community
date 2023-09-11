package com.majing.community;

import com.majing.community.controller.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

/*如果我们使用的是org.junit.jupiter.api包下的Test注解，那么我们是不需要@RunWith注解的。
而如果我们使用的是org.junit包下的Test注解，那么就需要@RunWith注解，如果我们不使用的话会出现空指针的情况*/
//@RunWith(SpringRunner.class)

//目的是加载ApplicationContext，启动spring容器。
@SpringBootTest
//指定配置文件
@ContextConfiguration(classes = CommunityApplication.class)
/*当一个类实现了ApplicationContextAware之后，
  这个类就可以方便获得ApplicationContext中的所有bean，
  这个类可以直接获取spring配置文件中，所有有引用到的bean对象。*/
class CommunityApplicationTests implements ApplicationContextAware{
	ApplicationContext applicationContext = null;
	@Test
	void contextLoads() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//当程序启动时，Spring容器application对自动传进来赋给实例变量
		this.applicationContext = applicationContext;
	}
	@Test
	public void applicationContextText(){
		//获取Spring容器
		System.out.println(applicationContext);
		//通过Spring容器对象获取Bean实例
		System.out.println(applicationContext.getBean(IndexController.class));
	}
}
