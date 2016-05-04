package mg.mvc.core;
/**
 * ajax结果对象
 * @author mg 
 * @date 2016-05-03
 *
 */
public class AjaxResult implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int state;//状态 200成功 0失败
	private String msg;//消息
	private Object data;//数据
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
