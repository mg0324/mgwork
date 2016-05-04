package mg.test;

import mg.ioc.annotation.ToBean;

@ToBean
public class TestDao {
	
	public void save(){
		System.out.println("保存test对象成功");
	}
}
