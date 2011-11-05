package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ESEEventTests extends UnitTest{

	Calendar cal = Calendar.getInstance();
	Date startDate;
	Date endDate;
	
	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;
	
	ESECalendar dummyCal = new ESECalendar("Dummy", null); /*for the moment id = 0*/
	
	@Before
	public void setUp(){		/* Sets up three Events for testing purpose*/
		cal.set(2011, 11, 24, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		endDate = cal.getTime();
		event1 = new ESEEvent("Weihnachten", dummyCal,startDate ,endDate , false);
		
		cal.set(2011, 11, 6, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 24, 19, 00);
		endDate = cal.getTime();
		event2 = new ESEEvent("Samichlaus", dummyCal,startDate ,endDate , true);
		
		cal.set(2011, 11, 24, 16, 00);
		startDate = cal.getTime();
		cal.set(2012, 01, 8, 18, 00);
		endDate = cal.getTime();
		event3 = new ESEEvent("Winter Ferien", dummyCal,startDate ,endDate , true);
	}
	
	@Test
	public void shouldBeInitialized(){
		/*Event 1*/
		assertTrue(event1 != null);
		assertEquals(0, event1.getEventID());
		assertTrue("Weihnachten".equals(event1.getEventName()));
		cal.set(2011, 11, 24, 18, 00);
		startDate = cal.getTime();
		assertEquals(startDate, event1.getStartDate());
		cal.set(2011, 11, 24, 23, 00);
		endDate = cal.getTime();
		assertEquals(endDate, event1.getEndDate());
		assertEquals(dummyCal, event1.getCorrespondingCalendar());
		
		/*Event 2*/
		assertTrue(event2 != null);
		assertEquals(1, event2.getEventID());
		assertTrue("Samichlaus".equals(event2.getEventName()));
		cal.set(2011, 11, 6, 18, 00);
		startDate = cal.getTime();
		assertEquals(startDate, event2.getStartDate());
		cal.set(2011, 11, 24, 19, 00);
		endDate = cal.getTime();
		assertEquals(endDate, event2.getEndDate());
		assertEquals(dummyCal, event2.getCorrespondingCalendar());
		
		/*Event 3*/
		assertTrue(event3 != null);
		assertEquals(2, event3.getEventID());
		assertTrue("Winter Ferien".equals(event3.getEventName()));
		cal.set(2011, 11, 24, 16, 00);
		startDate = cal.getTime();
		assertEquals(startDate, event3.getStartDate());
		cal.set(2012, 01, 8, 18, 00);
		endDate = cal.getTime();
		assertEquals(endDate, event3.getEndDate());
		assertEquals(dummyCal, event3.getCorrespondingCalendar());
	}
	
}
