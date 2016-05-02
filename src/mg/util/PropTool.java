package mg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties文件读取工具类
 * @author mg 
 * @date 2016-05-02
 *
 */
public class PropTool {
	private static Properties prop;
	
	public static Properties use(String path){
		prop = new Properties();
		InputStream is = PropTool.class.getClassLoader().getResourceAsStream(path);
		try {
			prop.load(is);
		} catch (IOException e) {
			System.out.println("未找到文件"+path);
		}
		return prop;
	}
}
