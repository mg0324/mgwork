package mg.ioc.core;

import java.util.Properties;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.mg.util.PropTool;

import mg.ioc.util.SerializeUtil;
import redis.clients.jedis.Jedis;

/**
 * ioc工厂 , 单例
 * @author mg
 * @date 2016-5-4
 */
public class IocFactory{
	/**
	 * 线程安全加锁,用来存放bean的容器
	 */
	//使用redis来做ioc的容器结构
	//private static Map<String, Object> factory = new HashMap<>();
	private static Jedis factory;
	static{
		Properties prop = PropTool.use("mgwork.properties");
		String host = prop.getProperty("mg.ioc.redis.host");
		int port = Integer.parseInt(prop.getProperty("mg.ioc.redis.port"));
		factory = new Jedis(host, port);
	}
	/**
	 * 加入factory容器中
	 * @param name 类名作为id
	 * @param newInstance 对象 
	 */
	public static void add(String name, Object newInstance) {
		synchronized (name) {
			factory.set(name.getBytes(), SerializeUtil.serialize(newInstance));
		}
	}
	/**
	 * 得到实例对象从容器中
	 * @param name id值
	 */
	public static Object get(String name){
		synchronized (name) {
			Object obj = SerializeUtil.unserialize(factory.get(name.getBytes()));
			if(obj == null){
				//未交给mgioc管理的，如mgwork的action是给servlet3.0web.xml容器管理的，不做处理
				return null;
			}
			return obj;
		}
	}
	/**
	 * 返回工厂的json字符串
	 * @return
	 */
	public static String toJsonString(){
		JSONObject json = new JSONObject();
		Set<String> keys = factory.keys("*");
		json.put("size", keys.size());
		json.put("ioc", keys);
		return json.toJSONString();
	}
	/**
	 * 释放factory
	 */
	public static void destoryFactory() {
		factory.flushDB();
	}
	
	/**
	 * 获取该对象实例从ioc容器中
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getBean(Class clazz){
		return SerializeUtil.unserialize(factory.get(clazz.getName().getBytes()));
	}
	
}
