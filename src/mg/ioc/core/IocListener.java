package mg.ioc.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;

import com.mg.log.MgLog;
import com.mg.util.PackageUtil;
import com.mg.util.PropTool;

import mg.ioc.annotation.ToBean;
import mg.ioc.annotation.UseBean;

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
	public void contextInitialized(ServletContextEvent sce) {
		initMgIoc();
	}
	/**
	 * 初始化mgioc
	 */
	private void initMgIoc() {
		//扫描注解加载到ioc工厂中
		//System.out.println("扫描注解加载到ioc工厂中");
		Properties prop = PropTool.use("mgwork.properties");
		//未配置就扫描所有package
		String packageName = prop.getProperty("mgioc.scan.package","");
		List<String> classNames = PackageUtil.getClassName(packageName);
		for(String className : classNames){
			injectToBean(className);
		}
		for(String className : classNames){
			injectUseBean(className);
		}
		MgLog.log.info("mgioc init success.");
		MgLog.log.info("ioc --> "+IocFactory.toJsonString());
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void injectUseBean(String className) {
		try {
			Class clazz = Class.forName(className);
			//解析属性注解@UseBean
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields){
				UseBean useBean = f.getAnnotation(UseBean.class);
				if(null != useBean){
					//System.out.println("className="+clazz.getName()+"的"+f.getName()+"属性 <-- 已被注入. ");
					//如果是webservlet3.0管理的mgwork的action的类，就不需注入了
					if(null != clazz.getAnnotation(WebServlet.class)) return ;
					
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
