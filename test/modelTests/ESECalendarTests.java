package modelTests;

import java.util.ArrayList;
import models.*;
import org.junit.*;

import controllers.Security;
import play.test.UnitTest;

public class ESECalendarTests extends UnitTest
{

	ESEUser ownerDummy;
	ESEUser ownerDummy2;
	ESECalendar cal1;
	ESECalendar cal2;
	ESEEvent event1;
	ESEEvent event2;
	ESEEvent event3;

	@Before
	public void setUp() throws ESEException
	{
		ESEDatabase.clearAll();

		this.ownerDummy = new ESEUser("dummy", "pw", "firstName", "familyName");
		this.ownerDummy2 = new ESEUser("dummy2", "pw2", "firstName", "familyName");
		this.cal1 = new ESECalendar("Cal1", this.ownerDummy);
		this.cal2 = new ESECalendar("Cal2", this.ownerDummy);

		//Calendar cal = Calendar.getInstance();
		/* create some Events */
		/*
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
		*/
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
	public void shouldAddEvent() throws ESEException
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
	public void shouldGetAllEvents() throws ESEException
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
	public void shouldGetAllEventsOfMonth() throws ESEException
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		try
		{
			this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		try
		{
			this.cal1.addEvent("Testevent5", "19.04.2011 12:00", "21.04.2011 14:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}

		assertEquals(5, this.cal1.getAllEventsOfMonth(3,2011).size());
	}

	@Test
	public void shouldGetAllPublicEventsOfMonth() throws ESEException
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		try
		{
			this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		try
		{
			this.cal1.addEvent("Testevent5", "19.04.2011 12:00", "21.04.2011 14:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}

		assertEquals(3, this.cal1.getAllPublicEventsOfMonth(3,2011).size());
	}

	@Test
	public void shouldGetAllAllowedEventsOfMonth() throws ESEException
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		try
		{
			this.cal1.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}

		//ESEDatabase.setCurrentUser(ownerDummy);

		assertEquals(4, this.cal1.getAllAllowedEventsOfMonth(3, 2011).size());

		//ESEDatabase.setCurrentUser(ownerDummy2);

		assertEquals(2, this.cal1.getAllAllowedEventsOfMonth(3, 2011).size());
	}

	@Test
	public void shouldGetAllPublicEvents() throws ESEException
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);

		assertEquals(1, this.cal1.getAllPublicEvents().size());
		assertEquals(3, this.cal1.getAllEvents().size());

		assertTrue(this.cal1.getAllPublicEvents().get(0).getEventName().equals("Testevent1"));
	}

	@Test
	public void shouldAddOverlappingEvents() throws ESEException
	{
		ESECalendar testCal = new ESECalendar("OverlapTest", this.ownerDummy);
		testCal.addEvent("Reference Event", "4.11.2011 16:00", "11.11.2011 18:00", true);
		try
		{
			testCal.addEvent("StartDate conflict", "7.11.2011 16:00", "14.11.2011 18:00", true);
			fail("StartDate conflict expected");
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		try
		{
			testCal.addEvent("EndDate conflict", "1.11.2011 16:00", "7.11.2011 18:00", true);
			fail("EndDate conflict expected");
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		try
		{
			testCal.addEvent("Contains conflict", "7.11.2011 16:00", "8.11.2011 18:00", true);
			fail("Contains conflict expected");
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		try
		{
			testCal.addEvent("Subset conflict", "1.11.2011 16:00", "16.11.2011 18:00", true);
			fail("Subset conflict expected");
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		assertEquals(5, testCal.getAllEvents().size());
	}

	@Test
	public void shouldGetAllAllowedDaysWithEvents() throws ESEException
	{
		this.cal1.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		this.cal1.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", true);
		this.cal1.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", true);
		this.cal1.addEvent("Testevent4", "18.04.2011 13:40", "19.04.2011 13:00", false);
 
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(13));
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(14));
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(15));
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(16));
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(17));
		assertTrue(this.cal1.getEventDaysOfMonth(3, 2011).contains(18));

		ArrayList<ESEEvent> publicEventsList = this.cal1.getAllAllowedEvents();
		assertEquals(3, publicEventsList.size());
		assertEquals("Testevent1", publicEventsList.get(0).getEventName());
		assertEquals("Testevent2", publicEventsList.get(1).getEventName());
		assertEquals("Testevent3", publicEventsList.get(2).getEventName());

	}

	@Test
	public void shouldRemoveEvent() throws ESEException
	{
		ESECalendar cal3 = new ESECalendar("Cal3", this.ownerDummy);
		cal3.addEvent("Testevent1", "13.04.2011 13:40", "14.04.2011 13:00", true);
		cal3.addEvent("Testevent2", "15.04.2011 13:40", "16.04.2011 13:00", false);
		cal3.addEvent("Testevent3", "17.04.2011 13:40", "18.04.2011 13:00", false);
		try
		{
			cal3.addEvent("Testevent4", "16.04.2011 13:40", "19.04.2011 13:00", true);
		}
		catch (ESEException e)
		{
			// No exception handling
			// Overlapping event added intentionally
		}
		cal3.removeEvent(3);
		assertEquals(3, cal3.getAllEvents().size());
		assertEquals("Testevent1", cal3.getAllEvents().get(0).getEventName());
		assertEquals("Testevent2", cal3.getAllEvents().get(1).getEventName());
		assertEquals("Testevent3", cal3.getAllEvents().get(2).getEventName());
	}

	@Test
	public void shouldReturnDaysOfEventInMonth() throws ESEException
	{
		this.cal2.addEvent("Long event", "14.10.2011 13:40", "17.12.2011 13:00", true);

		ArrayList<Integer> eventsInSeptember = this.cal2.getEventDaysOfMonth(8, 2011);
		assertEquals(0, eventsInSeptember.size());

		ArrayList<Integer> eventsInOctober = this.cal2.getEventDaysOfMonth(9, 2011);
		assertEquals(18, eventsInOctober.size());
		for (int i=14; i<=31; i++)
		{
			assertEquals(String.valueOf(i), String.valueOf(eventsInOctober.get(i-14)));
		}

		ArrayList<Integer> eventsInNovember = this.cal2.getEventDaysOfMonth(10, 2011);
		for (int i=1; i<=30; i++)
		{
			assertTrue(eventsInNovember.contains(i));
		}

		ArrayList<Integer> eventsInDecember = this.cal2.getEventDaysOfMonth(11, 2011);
		for (int i=1; i<=17; i++)
		{
			assertTrue(eventsInDecember.contains(i));
		}

		for (int i=18; i<=31; i++)
		{
			assertFalse(eventsInDecember.contains(i));
		}
	}
}
