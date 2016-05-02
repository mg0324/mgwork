package mg.core;

/**
 * @author 梅刚 2014-11-3 21:34
 * 
 */

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import mg.util.PropTool;

public abstract class MGWorkServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	/**
	 * request
	 */
	protected HttpServletRequest request;
	/**
	 * response
	 */
	protected HttpServletResponse response;
	/**
	 * 请求url
	 */
	public static String REQUEST_URL;
	/**
	 * 网页文件默认前缀/web-inf/pages
	 */
	private String MGWORK_WEBFLOADER_PREFIX = "/WEB-INF/pages";
	/**
	 * 网页文件后缀，默认.html
	 */
	private String MGWORK_WEB_PAGE_STUFFIX = ".html";
	/**
	 * 请求方法参数，默认action
	 */
	private String MGWORK_WEB_REQ_METHOD = "action";
	/**
	 * 模板，默认无，静态页
	 */
	private String MGWORK_WEB_VIEW_TYPE = "html";
	/**
	 * 视图后缀
	 */
	private String MGWORK_WEB_VIEW_TYPE_STUFFIX = ".html";
	
	
	
	private Configuration cfg;
	
	@Override
	public void init() throws ServletException {
		cfg = new Configuration();
		Properties prop = PropTool.use("mgwork.properties");
		MGWORK_WEBFLOADER_PREFIX = prop.getProperty("mgwork.webfolder.prefix","/WEB-INF/pages");
		MGWORK_WEB_PAGE_STUFFIX = prop.getProperty("mgwork.web.page.stuffix",".html");
		MGWORK_WEB_REQ_METHOD = prop.getProperty("mgwork.web.req.method", "action");
		MGWORK_WEB_VIEW_TYPE = prop.getProperty("mgwork.web.view.type", "html");
		MGWORK_WEB_VIEW_TYPE_STUFFIX = prop.getProperty("mgwork.web.view.type.stuffixe", ".html");
		
	}
	
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
		return request.getParameter(MGWORK_WEB_REQ_METHOD);
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
					//相对路径，绝对路径，后缀支持
					String tourl = res;
					if(!res.substring(0,1).equals("/")) tourl = MGWORK_WEBFLOADER_PREFIX + "/" + tourl;
					if(!res.contains(".")) tourl = tourl + MGWORK_WEB_PAGE_STUFFIX;
					//视图支持
					if(MGWORK_WEB_VIEW_TYPE.equals("freemarker")){
						//freemarker
						if(!res.contains(".")) res = res + MGWORK_WEB_VIEW_TYPE_STUFFIX;
						handleFreemarker(res);
						request.getRequestDispatcher(tourl);//不需要转发，对于freemarker
					}else{
						request.getRequestDispatcher(tourl).forward(request,response);
					}
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
	 * freemarker支持
	 */
	private void handleFreemarker(String tpl) {
		//获取request中的attributes到data中，然后给freemarker渲染
		Map<String,Object> data = new HashMap<String,Object>();
		//封装request中的参数
		for (Enumeration<String> attrs=request.getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			data.put(attrName, request.getAttribute(attrName));
		}
		//封装session中参数
		for (Enumeration<String> attrs=request.getSession().getAttributeNames(); attrs.hasMoreElements();) {
			String attrName = attrs.nextElement();
			data.put(attrName, request.getSession().getAttribute(attrName));
		}
		cfg.setServletContextForTemplateLoading(getServletContext(),MGWORK_WEBFLOADER_PREFIX);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setNumberFormat("#0.#####");
        cfg.setDateFormat("yyyy-MM-dd");
        cfg.setTimeFormat("HH:mm:ss");
        cfg.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        Template temp;
        Writer out = null;
		try {
			temp = cfg.getTemplate(tpl);
			out = new OutputStreamWriter(response.getOutputStream());
		    temp.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null)
				try {
					out.close();
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
				if(val[0].length()>0) json.put(key, val[0]);
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
				if(val[0].length()>0) json.put(key, val[0]);
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
				if(val[0].length()>0) map.put(key, val[0]);
			}
		}
		return map;
	}
	/**增加request的方法**/
	protected void setAttr(String key,Object v) {
		this.request.setAttribute(key, v);
	}
	protected Object getAttr(String key) {
		return this.request.getAttribute(key);
	}
	
	protected void setSessionAttr(String key,Object v) {
		this.request.getSession().setAttribute(key, v);
	}
	protected Object getSessionAttr(String key) {
		return this.request.getSession().getAttribute(key);
	}
}
