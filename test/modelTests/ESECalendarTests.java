package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.ESECalendar;
import models.ESEEvent;
import models.ESEUser;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

public class ESECalendarTests extends UnitTest{

	ESEUser ownerDummy = new ESEUser(0, "dummy", "pw", "firstName", "familyName");
	ESECalendar cal1;
	ESECalendar cal2;
	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;
		
	@Before
	public void setUp(){
		
		cal1 = new ESECalendar(0, "Cal1", ownerDummy);
		cal2 = new ESECalendar(1, "Cal2", ownerDummy);
		
		Calendar cal = Calendar.getInstance();
		/*create some Events*/
		cal.set(2011, 11, 24, 18, 00);
		Date startDate = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		Date endDate = cal.getTime();
		event1 = new ESEEvent("Event1", cal1,startDate ,endDate , true);
		
		cal.set(2011, 11, 25, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 25, 23, 00);
		endDate = cal.getTime();
		event2 = new ESEEvent("Event2", cal1,startDate ,endDate , true);
		
		cal.set(2011, 11, 26, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 26, 23, 00);
		endDate = cal.getTime();
		event3 = new ESEEvent("Event3", cal1,startDate ,endDate , true);
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(cal1 != null);
		assertEquals(0, cal1.getID());
		assertEquals(ownerDummy, cal1.getOwner());
		
		assertTrue(cal2 != null);
		assertEquals(1, cal2.getID());
		assertEquals(ownerDummy, cal2.getOwner());
	}
	
	@Test
	public void shouldAddEvent(){
		cal1.addEvent(event1);
		assertTrue(cal1.getAllEvents() != null);
	}
	@Test
	public void shouldGetAllEvents(){
		
	}
	
	@Test
	public void shouldGetAllPublicEvents(){
		
	}
	
}

