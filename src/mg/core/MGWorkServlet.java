package mg.core;

/**
 * @author 梅刚 2014-11-3 21:34
 * 
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public abstract class MGWorkServlet extends HttpServlet{
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	/**
	 * 请求url
	 */
	public static String REQUEST_URL;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response;
		//编码过滤
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		REQUEST_URL = request.getRequestURL().toString() +"?"+ request.getQueryString();
		runMethod();
	}
	/**
	 * 1.分析url得到要执行的方法名
	 * @param r
	 */
	public String getActionNameFromUrl(){
		return request.getParameter("action");
	}
	/**
	 * 根据方法名，使用反射得到该方法对象
	 * @param r
	 * @return
	 */
	public Method getMethodByActionName(){
		Method m = null;
		try {
			m = this.getClass().getMethod(getActionNameFromUrl());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return m;
	}
	/**
	 * 使用invoke来运行要执行的Method对象
	 * @param r
	 */
	public void runMethod(){
		Method m = getMethodByActionName();
		if(m==null){
			//如果请求的方法不存在
			try {
				PrintWriter out = response.getWriter();
				String errorHtml = "<head><meta charset='utf-8'/><title>404</title></head><h2><center>404</center></h2>"
						+ "<hr/><center>您输入的请求有误，无法访问到资源，请重新输入!!<br/>shared by <a href=''>mgwork1.0</a></center>";
				out.print(errorHtml);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			String res;
			try {
				res = (String) m.invoke(this);
				if(res!=null){
					request.getRequestDispatcher(res).forward(request,response);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 封装request中参数为对象
	 * @param c 将表单参数要转成的对象类型，eg User.class
	 * @return 返回封装好的对象，通过fastjson to object实现
	 */
	@SuppressWarnings("unchecked")
	protected Object mgf2Object(@SuppressWarnings("rawtypes") Class c) {
		JSONObject json = new JSONObject();
		Map<String, String[]> p = this.request.getParameterMap();
		for(String key : p.keySet()){
			String[] val = p.get(key);
			if(val.length>1){
				//checkbox , mutli select
				//支持多表单属性，用英文逗号间隔
				String _v = "0";
				for(String v : val){
					_v += "," + v;
				}
				json.put(key, _v.substring(2));
			}else{
				json.put(key, val[0]);
			}
		}
		return JSONObject.toJavaObject(json, c);
	}
	
	/**
	 * 封装request中参数为json
	 * @return JSONObject
	 */
	protected JSONObject mgf2Json() {
		JSONObject json = new JSONObject();
		Map<String, String[]> p = this.request.getParameterMap();
		for(String key : p.keySet()){
			String[] val = p.get(key);
			if(val.length>1){
				//checkbox , mutli select
				//支持多表单属性，用英文逗号间隔
				String _v = "0";
				for(String v : val){
					_v += "," + v;
				}
				json.put(key, _v.substring(2));
			}else{
				json.put(key, val[0]);
			}
		}
		return json;
	}
	
	/**
	 * 封装request中参数为map
	 * @return Map
	 */
	protected Map<String,String> mgf2Map() {
		Map<String,String> map = new HashMap<String,String>();
		Map<String, String[]> p = this.request.getParameterMap();
		for(String key : p.keySet()){
			String[] val = p.get(key);
			if(val.length>1){
				//checkbox , mutli select
				//支持多表单属性，用英文逗号间隔
				String _v = "0";
				for(String v : val){
					_v += "," + v;
				}
				map.put(key, _v.substring(2));
			}else{
				map.put(key, val[0]);
			}
		}
		return map;
	}
}
