package mg.test;

import mg.ioc.annotation.ToBean;
import mg.ioc.annotation.UseBean;

@ToBean
public class TestService {
	@UseBean
	private TestDao testDao;
	
	public void save(){
		testDao.save();
	}
}
