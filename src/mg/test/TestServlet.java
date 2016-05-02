package mg.test;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.apache.tomcat.util.security.MD5Encoder;

import mg.core.MGWorkServlet;
@WebServlet("/test.mg")
public class TestServlet extends MGWorkServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		return "jsp/demo.jsp";
	}
	
	public String testjsp(){
		this.setSessionAttr("time", new Date().toLocaleString());
		User u = new User();
		u.setAge(12);
		u.setBirthday(new Date());
		u.setHobby("长街，跳水");
		u.setPassword(MD5Encoder.encode("123456".getBytes()));
		u.setUsername("xiaogang");
		this.setAttr("user", u);
		return "jsp/demo.jsp";
	}

}
