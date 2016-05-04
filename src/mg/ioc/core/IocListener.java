package mg.ioc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mg.ioc.annotation.ToBean;
import mg.ioc.annotation.UseBean;
import mg.util.PackageUtil;

/**
 * ioc监听，随web.xml启动 , 单例
 * @author mg
 * @date 2016-5-4
 */
public class IocListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		destoryMgIoc();
	}
	/**
	 * 销毁mgioc
	 */
	private void destoryMgIoc() {
		IocFactory.destoryFactory();
		//释放工厂资源
		System.out.println("mgioc释放资源成功");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		initMgIoc();
	}
	/**
	 * 初始化mgioc
	 */
	private void initMgIoc() {
		//扫描注解加载到ioc工厂中
		//System.out.println("扫描注解加载到ioc工厂中");
		String packageName = "mg.test";
		List<String> classNames = PackageUtil.getClassName(packageName);
		for(String className : classNames){
			injectToBean(className);
		}
		for(String className : classNames){
			injectUseBean(className);
		}
		System.out.println("mgioc init success.");
		System.out.println("ioc --> "+IocFactory.toJsonString());
	}
	/**
	 * 解析ToBean注解
	 * @param className 类名
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void injectToBean(String className) {
		try {
			Class clazz = Class.forName(className);
			//解析类注解@ToBean
			Annotation toBean = clazz.getAnnotation(ToBean.class);
			if(null != toBean){
				//System.out.println("className="+className+" --> 加入到mgioc容器中. ");
				IocFactory.add(clazz.getName(),clazz.newInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解析UseBean注解
	 * @param className 类名
	 */
	@SuppressWarnings("rawtypes")
	private void injectUseBean(String className) {
		try {
			Class clazz = Class.forName(className);
			//解析属性注解@UseBean
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				UseBean useBean = f.getAnnotation(UseBean.class);
				if(null != useBean){
					//System.out.println("className="+clazz.getName()+"的"+f.getName()+"属性 <-- 已被注入. ");
					//打开访问private的属性
					f.setAccessible(true);
					f.set(IocFactory.get(clazz.getName()), IocFactory.get(f.getType().toString().split(" ")[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
