package hbu.logic;

import hbu.spring.di.BeanFactory;
import hbu.spring.di.XmlClasspathConfiguration;

public class Test {
	public static void main(String[] args) {
		BeanFactory beanFactory=new XmlClasspathConfiguration("beans.xml");
		UserService userService=(UserService) beanFactory.getBean("userService");
		userService.add();
	}
}
