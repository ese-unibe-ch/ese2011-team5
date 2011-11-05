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
		event2 = new ESEEvent("Event2", cal1,startDate ,endDate , false);
		
		cal.set(2011, 11, 26, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 26, 23, 00);
		endDate = cal.getTime();
		event3 = new ESEEvent("Event3", cal1,startDate ,endDate , false);
	}
	
	@Test
	public void shouldInitialize(){
		assertTrue(cal1 != null);
		assertTrue(cal1.getAllEvents() != null);
		assertEquals(0, cal1.getID());
		assertEquals(ownerDummy, cal1.getOwner());
		
		assertTrue(cal2 != null);
		assertTrue(cal2.getAllEvents() != null);
		assertEquals(1, cal2.getID());
		assertEquals(ownerDummy, cal2.getOwner());
	}
	
	@Test
	public void shouldAddEvent(){
		cal1.addEvent(event1);
		assertEquals(1, cal1.getAllEvents().size());
		assertEquals(1, cal1.getAllPublicEvents().size());
		cal1.addEvent(event2);
		assertEquals(2, cal1.getAllEvents().size());
		assertEquals(1, cal1.getAllPublicEvents().size());
		
		cal2.addEvent(event3);
		assertEquals(1, cal2.getAllEvents().size());
		assertEquals(0, cal2.getAllPublicEvents().size());
		assertEquals(2, cal1.getAllEvents().size());
		assertEquals(1, cal1.getAllPublicEvents().size());
	}
	@Test
	public void shouldGetAllEvents(){
		cal1.addEvent(event1);
		cal1.addEvent(event2);
		cal1.addEvent(event3);
		
		assertEquals(1, cal1.getAllPublicEvents().size());
		assertEquals(3, cal1.getAllEvents().size());
		
		assertTrue(cal1.getAllEvents().contains(event1));
		assertTrue(cal1.getAllEvents().contains(event2));
		assertTrue(cal1.getAllEvents().contains(event3));

	}
	
	@Test
	public void shouldGetAllPublicEvents(){
		cal1.addEvent(event1);
		cal1.addEvent(event2);
		cal1.addEvent(event3);
		
		assertEquals(1, cal1.getAllPublicEvents().size());
		assertEquals(3, cal1.getAllEvents().size());
		
		assertTrue(cal1.getAllPublicEvents().contains(event1));
	}
	
}

