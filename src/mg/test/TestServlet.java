package mg.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.apache.tomcat.util.security.MD5Encoder;

import com.alibaba.fastjson.JSONObject;

import mg.core.MGWorkServlet;
@WebServlet("/test.mg/*")
public class TestServlet extends MGWorkServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String index(){
		
		return "index";
	}
	/**
	 * test1方法
	 * @return 跳转到test1.html
	 */
	public String test1(){
		
		return "test1";
	}
	
	/**
	 * test2方法
	 * @return 跳转到test2.html
	 */
	public String test2(){
		
		return "test2";
	}
	
	public String wrapfrm(){
		this.setSessionAttr("time", new Date().toLocaleString());
		User u = (User) mgf2Object(User.class);
		this.setAttr("user", u);
		return "freemarker/demo1";
	}
	
	public void testjsp(){
		this.setSessionAttr("time", new Date().toLocaleString());
		User u = new User();
		u.setAge(12);
		u.setBirthday(new Date());
		u.setHobby("长街，跳水");
		u.setPassword(MD5Encoder.encode("123456".getBytes()));
		u.setUsername("xiaogang");
		this.setAttr("user", u);
		renderJsp("jsp/demo");
		
	}
	
	public void testfreemarker(){
		this.setSessionAttr("time", new Date().toLocaleString());
	
		List<User> list = new ArrayList<User>();
		for(int i=0;i<10;i++){
			User u = new User();
			u.setAge(12+i);
			u.setBirthday(new Date());
			u.setHobby("长街，跳水");
			u.setPassword(Math.random()+"");
			u.setUsername("xiaogang"+i);
			list.add(u);
		}
		this.setAttr("ulist", list);
		this.renderFreemarker("freemarker/demo");
	}
	
	public void testAjax(){
		JSONObject json = this.mgf2Json();
		this.ajaxJsonSuccess(json);
	}

}
