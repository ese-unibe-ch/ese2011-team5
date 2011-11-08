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
		assertEquals(0, this.cal1.getID());
		assertEquals(this.ownerDummy, this.cal1.getOwner());

		assertTrue(this.cal2 != null);
		assertTrue(this.cal2.getAllEvents() != null);
		assertEquals(1, this.cal2.getID());
		assertEquals(this.ownerDummy, this.cal2.getOwner());
	}

	@Test
	public void shouldAddEvent() {
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		assertEquals(1, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		assertEquals(2, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());

		this.cal2.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);
		assertEquals(1, this.cal2.getAllEvents().size());
		assertEquals(0, this.cal2.getAllPublicEvents().size());
		assertEquals(2, this.cal1.getAllEvents().size());
		assertEquals(1, this.cal1.getAllPublicEvents().size());
	}

	@Test
	public void shouldDeliberatelyAddEventToDifferentCalendar()
	{
		ESEUser user1 = new ESEUser("user1", "pw", "first", "family");
		ESEUser user2 = new ESEUser("user2", "pass", "Name", "Name");
		user1.addCalendar("First");
		ESECalendar first = user1.getCalendarList().get(0);
		System.out.println("first: "+first.getID());
		user1.addCalendar("Second");
		ESECalendar second = user1.getCalendarList().get(1);
		System.out.println("second: "+second.getID());
		user2.addCalendar("Other");
		ESECalendar other = user2.getCalendarList().get(0);
		System.out.println("other: "+other.getID());

		// Error occurs here, when corresponding calendar is different
		first.addEvent("FirstInSecond", second, "7.11.2001 18:00", "7.11.2001 18:00", true);
		first.addEvent("FirstInOther", other, new Date(), new Date(), true);

		assertEquals(0, second.getAllEvents().size());
		assertEquals(0, other.getAllEvents().size());

		assertEquals(2, user1.getAllEvents(first.getID()).size());

		assertEquals(0, user1.getAllEvents(second.getID()).size());
		assertEquals(0, user2.getAllEvents(other.getID()).size());

		// These checks trigger the error
		assertEquals(first.getCalendarName(), user1.getAllEvents(first.getID()).get(0).getCorrespondingCalendar().getCalendarName());
		assertEquals(first.getCalendarName(), user1.getAllEvents(first.getID()).get(1).getCorrespondingCalendar().getCalendarName());
	}

	@Test
	public void shouldGetAllEvents() {
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllEvents().get(0).getEventName().equals("Testevent1"));
		assertTrue(this.cal1.getAllEvents().get(1).getEventName().equals("Testevent2"));
		assertTrue(this.cal1.getAllEvents().get(2).getEventName().equals("Testevent3"));
	}
	
	@Test
	public void shouldGetAllEventsOfDay(){
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", this.cal1, "16.04.2011 13:40", "19.04.2011 13:00", true);
		this.cal1.addEvent("Testevent5", this.cal1, "19.04.2011 12:00", "21.04.2011 14:00", true);

		assertEquals(1, this.cal1.getAllEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("14.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("16.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("18.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("19.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("20.04.2011 13:00").size());
	}
	
	@Test
	public void shouldGetAllPublicEventsOfDay(){
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", this.cal1, "16.04.2011 13:40", "19.04.2011 13:00", true);
		this.cal1.addEvent("Testevent5", this.cal1, "19.04.2011 12:00", "21.04.2011 14:00", true);
		
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("14.04.2011 13:50").size());
		assertEquals(0, this.cal1.getAllPublicEventsOfDay("15.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("16.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("17.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("18.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllPublicEventsOfDay("19.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("20.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllPublicEventsOfDay("21.04.2011 13:50").size());
	}
	
	@Test
	public void shouldGetAllAllowedEventsOfDay(){
		
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addEvent("Testevent4", this.cal1, "16.04.2011 13:40", "19.04.2011 13:00", true);
		
		ESEDatabase.getInstance().setCurrentUser(ownerDummy);
		
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("14.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllAllowedEventsOfDay("16.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllAllowedEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("19.04.2011 13:50").size());
		
		ESEDatabase.getInstance().setCurrentUser(ownerDummy2);
		
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("14.04.2011 13:50").size());
		assertEquals(0, this.cal1.getAllAllowedEventsOfDay("15.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("18.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllAllowedEventsOfDay("19.04.2011 13:50").size());
	}
	

	@Test
	public void shouldGetAllPublicEvents() {
		this.cal1.addEvent("Testevent1", this.cal1, "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", this.cal1, "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", this.cal1, "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllPublicEvents().get(0).getEventName().equals("Testevent1"));
	}

	@Test
	public void shouldAvoidOverlappingEvents()
	{
		ESECalendar testCal = new ESECalendar("OverlapTest", this.ownerDummy);
		testCal.addEvent("Reference Event", testCal, "4.11.2011 16:00", "11.11.2011 18:00", true);

		try
		{
			testCal.addEvent("StartDate conflict", testCal, "7.11.2011 16:00", "14.11.2011 18:00", true);
			fail("StartDate conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{		
			testCal.addEvent("EndDate conflict", testCal, "1.11.2011 16:00", "7.11.2011 18:00", true);
			fail("EndDate conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{
			testCal.addEvent("Contains conflict", testCal, "7.11.2011 16:00", "8.11.2011 18:00", true);
			fail("Contains conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{
			testCal.addEvent("Subset conflict", testCal, "1.11.2011 16:00", "16.11.2011 18:00", true);
			fail("Subset conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		assertEquals(1, testCal.getAllEvents().size());
		assertEquals("Reference Event", testCal.getAllEventsOfDay("7.11.2011 17:00").get(0).getEventName());
		assertEquals(testCal.getAllEventsOfDay("4.11.2011 15:00"), testCal.getAllEventsOfDay("11.11.2011 19:00"));
	}
}
