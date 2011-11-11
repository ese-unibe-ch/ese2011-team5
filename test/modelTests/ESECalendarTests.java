package modelTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import models.*;
import org.junit.*;

public class ESECalendarTests
{

	ESEUser ownerDummy = new ESEUser("dummy", "pw", "firstName", "familyName");
	ESEUser ownerDummy2 = new ESEUser("dummy2", "pw2", "firstName", "familyName");
	ESECalendar cal1;
	ESECalendar cal2;
	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;

	@Before
	public void setUp()
	{
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
	public void shouldInitialize()
	{
		assertTrue(this.cal1 != null);
		assertTrue(this.cal1.getAllEvents() != null);
		assertEquals(this.ownerDummy, this.cal1.getOwner());

		assertTrue(this.cal2 != null);
		assertTrue(this.cal2.getAllEvents() != null);
		assertEquals(this.ownerDummy, this.cal2.getOwner());
	}

	@Test
	public void shouldAddEvent()
	{
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
	public void shouldGetAllEvents()
	{
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
	public void shouldGetAllEventsOfDay()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addOverlappingEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		this.cal1.addOverlappingEvent("Testevent5", "19.04.2011 12:00", "21.04.2011 14:00", true);

		assertEquals(1, this.cal1.getAllEventsOfDay("13.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("14.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("16.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("18.04.2011 13:50").size());
		assertEquals(2, this.cal1.getAllEventsOfDay("19.04.2011 13:50").size());
		assertEquals(1, this.cal1.getAllEventsOfDay("20.04.2011 13:00").size());
	}

	@Test
	public void shouldGetAllPublicEventsOfDay()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addOverlappingEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		this.cal1.addOverlappingEvent("Testevent5", "19.04.2011 12:00", "21.04.2011 14:00", true);

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
	public void shouldGetAllAllowedEventsOfDay()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addOverlappingEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);

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
	public void shouldGetAllPublicEvents()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllPublicEvents().get(0).getEventName().equals("Testevent1"));
	}

	@Test
	public void shouldAvoidOverlappingEvents()
	{
		ESECalendar testCal = new ESECalendar("OverlapTest", this.ownerDummy);
		testCal.addEvent("Reference Event", "4.11.2011 16:00", "11.11.2011 18:00", true);
		try
		{
			testCal.addEvent("StartDate conflict", "7.11.2011 16:00", "14.11.2011 18:00", true);
			fail("StartDate conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{
			testCal.addEvent("EndDate conflict", "1.11.2011 16:00", "7.11.2011 18:00", true);
			fail("EndDate conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{
			testCal.addEvent("Contains conflict", "7.11.2011 16:00", "8.11.2011 18:00", true);
			fail("Contains conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		try
		{
			testCal.addEvent("Subset conflict", "1.11.2011 16:00", "16.11.2011 18:00", true);
			fail("Subset conflict expected");
		}
		catch (IllegalArgumentException e)
		{}
		assertEquals(1, testCal.getAllEvents().size());
		assertEquals("Reference Event", testCal.getAllEventsOfDay("7.11.2011 17:00").get(0).getEventName());
		assertEquals(testCal.getAllEventsOfDay("4.11.2011 15:00"), testCal.getAllEventsOfDay("11.11.2011 19:00"));
	}

	@Test
	public void shouldGetAllAllowedDaysWithEvents()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", true);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", true);
		this.cal1.addEvent("Testevent4", "18.04.2011 13:40", "19.04.2011 13:00", false);
 
		assertTrue(cal1.getEventDaysOfMonth(3).contains(13));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(14));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(15));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(16));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(17));
		assertTrue(cal1.getEventDaysOfMonth(3).contains(18));

		ArrayList<ESEEvent> publicEventsList = cal1.getAllAllowedEvents();
		assertEquals(3, publicEventsList.size());
		assertEquals("Testevent1", publicEventsList.get(0).getEventName());
		assertEquals("Testevent2", publicEventsList.get(1).getEventName());
		assertEquals("Testevent3", publicEventsList.get(2).getEventName());

	}

	@Test
	public void shouldRemoveEvent()
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		this.cal1.addOverlappingEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		this.cal1.removeEvent(70);
		assertEquals(3, this.cal1.getAllEvents().size());
		assertEquals("Testevent1", this.cal1.getAllEvents().get(0).getEventName());
		assertEquals("Testevent2", this.cal1.getAllEvents().get(1).getEventName());
		assertEquals("Testevent3", this.cal1.getAllEvents().get(2).getEventName());
		
	}

	@Test
	public void shouldDaysOfEventInMonth()
	{
		this.cal2.addEvent("Long event", "14.10.2011 13:40", "17.12.2011 13:00", true);

		ArrayList<Integer> eventsInSeptember = this.cal2.getEventDaysOfMonth(8);
		assertEquals(0, eventsInSeptember.size());

		ArrayList<Integer> eventsInOctober = this.cal2.getEventDaysOfMonth(9);
		assertEquals(18, eventsInOctober.size()); // Expect [14, 15, ..., 30, 31]
		for (int i = 14; i <= 31; i++)
		{
			assert i == eventsInOctober.get(i);
		}

		ArrayList<Integer> eventsInNovember = this.cal2.getEventDaysOfMonth(10);
		assertEquals(30, eventsInNovember.size()); // Expect [1, 2, ..., 29, 30]
		for (int i = 1; i <= 30; i++)
		{
			assert i == eventsInNovember.get(i);
		}

		ArrayList<Integer> eventsInDecember = this.cal2.getEventDaysOfMonth(11);
		assertEquals(17, eventsInDecember.size()); // Expect [1, 2, ..., 16, 17]
		for (int i = 1; i <= 17; i++)
		{
			assert i == eventsInDecember.get(i);
		}
	}
}
