package mg.ioc.core;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mg.log.MgLog;
import com.mg.util.PackageUtil;
import com.mg.util.PropTool;

import mg.ioc.annotation.ToBean;

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
		MgLog.log.info("mgioc init success.");
		MgLog.log.info("mgioc --> "+IocFactory.toJsonString());
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
	
}
