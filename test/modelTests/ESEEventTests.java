package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;

import org.junit.Before;

import play.test.UnitTest;

public class ESEEventTests extends UnitTest{

	Calendar cal = Calendar.getInstance();
	Date startEvent;
	Date endEvent;
	
	ESECalendar calendar = new ESECalendar(calendarID, "Dummy", null);
	
	@Before
	public void setUp(){
		cal.set(2011, 11, 24, 18, 00);
		startEvent = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		ESEEvent event1 = new ESEEvent("Weihnachten", startEvent, endEvent, null, true);
	}
	
}
