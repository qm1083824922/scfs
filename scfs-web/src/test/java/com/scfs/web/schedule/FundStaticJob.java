package com.scfs.web.schedule;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired; 
import com.scfs.service.schedule.FundStatisticsJob;
import com.scfs.web.base.BaseJUnitTest;

public class FundStaticJob extends BaseJUnitTest{
	@Autowired
	private FundStatisticsJob fundStatisticsJob;
	
	@Test
	public void testExcute(){
		fundStatisticsJob.execute();
	}
	
	//date
	public static void main(String[] args) { 
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);  
 		System.out.println(c.get(Calendar.YEAR));
		System.out.println(c.get(Calendar.MONTH )); 
		System.out.println(c.get(Calendar.DATE )); 
	}
	
}
