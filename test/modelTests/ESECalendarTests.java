package modelTests;

import java.util.Calendar;
import java.util.Date;

import models.*;

import org.junit.*;

import play.test.UnitTest;

public class ESECalendarTests extends UnitTest {

	ESEUser ownerDummy = new ESEUser("dummy", "pw", "firstName", "familyName");
	ESEUser ownerDummy2 = new ESEUser("dummy2", "pw2", "firstName", "familyName");
	ESECalendar cal1;
	ESECalendar cal2;
	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;

	@Before
	public void setUp() {

		this.cal1 = new ESECalendar("Cal1", this.ownerDummy);
		this.cal2 = new ESECalendar("Cal2", this.ownerDummy);

		Calendar cal = Calendar.getInstance();
		/* create some Events */
		cal.set(2011, 11, 24, 18, 00);
		Date startDate = cal.getTime();
		cal.set(2011, 11, 24, 23, 00);
		Date endDate = cal.getTime();
		this.event1 = new ESEEvent("Event1", this.cal1, startDate, endDate, true);

		cal.set(2011, 11, 25, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 25, 23, 00);
		endDate = cal.getTime();
		this.event2 = new ESEEvent("Event2", this.cal1, startDate, endDate, false);

		cal.set(2011, 11, 26, 18, 00);
		startDate = cal.getTime();
		cal.set(2011, 11, 26, 23, 00);
		endDate = cal.getTime();
		this.event3 = new ESEEvent("Event3", this.cal1, startDate, endDate, false);
	}

	@Test
	public void shouldInitialize() {		
		assertTrue(this.cal1 != null);
		assertTrue(this.cal1.getAllEvents() != null);
		assertEquals(this.ownerDummy, this.cal1.getOwner());

		assertTrue(this.cal2 != null);
		assertTrue(this.cal2.getAllEvents() != null);
		assertEquals(this.ownerDummy, this.cal2.getOwner());
	}

	@Test
	public void shouldAddEvent() {
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		assertEquals(1, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		assertEquals(2, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());

		this.cal2.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		assertEquals(1, this.cal2.getAllEvents().size());
		assertEquals(0, this.cal2.getAllPublicEvents().size());
		assertEquals(2, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());
	}

	@Test
	public void shouldGetAllEvents() {
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllEvents().get(0).getEventName().equals("Testevent1"));
		assertTrue(this.cal1.getAllEvents().get(1).getEventName().equals("Testevent2"));
		assertTrue(this.cal1.getAllEvents().get(2).getEventName().equals("Testevent3"));
	}
	
	@Test
	public void shouldGetAllEventsOfDay(){
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		
		assertEquals(1, this.cal1.getAllEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("14.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("16.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("19.04.2011 13:50").size());
	}
	
	@Test
	public void shouldGetAllPublicEventsOfDay(){
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("14.04.2011 13:50").size());
		assertEquals(0, this.cal1.getAllPublicEventsOfDay("15.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("19.04.2011 13:50").size());
	}
	
	@Test
	public void shouldGetAllAllowedEventsOfDay(){
		
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		
		ESEDatabase.setCurrentUser(ownerDummy);
		
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("14.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllAllowedEventsOfDay("16.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllAllowedEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("19.04.2011 13:50").size());
		
		ESEDatabase.setCurrentUser(ownerDummy2);
		
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("14.04.2011 13:50").size());
		assertEquals(0, this.cal1.getAllAllowedEventsOfDay("15.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("19.04.2011 13:50").size());
	}
	

	@Test
	public void shouldGetAllPublicEvents() {
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllPublicEvents().get(0).getEventName().equals("Testevent1"));
	}

	@Test
	public void shouldGetAllDaysWithEvents() {
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		
		assertTrue(cal1.getEventDaysOfMonth(3).contains(13));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(14));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(15));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(16));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(17));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(18));
	}

	
}
