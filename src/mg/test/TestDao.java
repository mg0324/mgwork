package mg.test;

import java.io.Serializable;

import mg.ioc.annotation.ToBean;

@ToBean
public class TestDao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void save(){
		System.out.println("保存test对象成功");
	}
}
