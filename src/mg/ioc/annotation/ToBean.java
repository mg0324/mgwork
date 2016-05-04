package mg.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 类注解，注入到mgioc容器中
 * @author mg
 * @date 2016-5-4
 *
 */
@Target(ElementType.TYPE)//类注解
@Retention(RetentionPolicy.RUNTIME)//运行时环境
public @interface ToBean {
	
}
