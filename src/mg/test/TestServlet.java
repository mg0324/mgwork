package mg.test;

import javax.servlet.annotation.WebServlet;

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
		System.out.println(mgf2Object(User.class));
		System.out.println(mgf2Json());
		System.out.println(mgf2Map());
		return "/index";
	}

}
