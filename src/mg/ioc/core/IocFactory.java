package mg.ioc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ioc工厂 , 单例
 * @author mg
 * @date 2016-5-4
 */
public class IocFactory{
	/**
	 * 线程安全加锁,用来存放bean的容器
	 */
	private static Map<String, Object> factory = new HashMap<>();
	/**
	 * 加入factory容器中
	 * @param name 类名作为id
	 * @param newInstance 对象 
	 */
	public static void add(String name, Object newInstance) {
		synchronized (name) {
			factory.put(name, newInstance);
		}
	}
	/**
	 * 得到实例对象从容器中
	 * @param name id值
	 */
	@SuppressWarnings("rawtypes")
	public static Object get(String name){
		synchronized (name) {
			Object obj = factory.get(name);
			if(obj == null){
				//未交给mgioc管理的，如mgwork的action是给servlet3.0web.xml容器管理的，需要手动创建一个对象管理起来
				try {
					Class clazz = Class.forName(name);
					obj = clazz.newInstance();
					IocFactory.add(name, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return obj;
		}
	}
	/**
	 * 返回工厂的json字符串
	 * @return
	 */
	public static String toJsonString(){
		return factory.toString();
	}
	/**
	 * 释放factory
	 */
	public static void destoryFactory() {
		factory.clear();
	}
	
	/**
	 * 获取该对象实例从ioc容器中
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getBean(Class clazz){
		return factory.get(clazz.getName());
	}
	
}
