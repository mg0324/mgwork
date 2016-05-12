package mg.test;

import java.io.Serializable;

import mg.ioc.annotation.ToBean;
import mg.ioc.annotation.UseBean;

@ToBean
public class TestService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@UseBean
	private TestDao testDao;
	
	public void save(){
		testDao.save();
	}
}
