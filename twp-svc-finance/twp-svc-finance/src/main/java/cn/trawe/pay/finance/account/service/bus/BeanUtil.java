package cn.trawe.pay.finance.account.service.bus;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements org.springframework.context.ApplicationContextAware {
	
		private static ApplicationContext ctx;
	 
		public BeanUtil() {
		}
	 
		public static Object getBean(String name) {
			return ctx.getBean(name);
		}
	 
		public static <T> T getBean(Class<T> requiredType) {
			return ctx.getBean(requiredType);
		}
	 
		public static <T> T getBean(String name, Class<T> requiredType) {
			return ctx.getBean(name, requiredType);
		}
	 
		public static <T> T getBean(Class<T> requiredType, Object... args) {
			return ctx.getBean(requiredType, args);
		}
	 

		public static ApplicationContext getApplicationContext() {
			return ctx;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			ctx = applicationContext;
			
		}
	}